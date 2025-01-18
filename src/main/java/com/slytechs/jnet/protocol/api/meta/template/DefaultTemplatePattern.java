package com.slytechs.jnet.protocol.api.meta.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.slytechs.jnet.protocol.api.meta.DefaultFormats;
import com.slytechs.jnet.protocol.api.meta.FormatRegistry;
import com.slytechs.jnet.protocol.api.meta.ValueFormatter.SpecificValueFormatter;

public class DefaultTemplatePattern implements PlaceholderPattern {

	record SplitArg(String expression, String[] split) implements Placeholder {

		@Override
		public boolean isFormatPresent() {
			return split.length == 2;
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern.Placeholder#referenceName()
		 */
		@Override
		public String referenceName() {
			return split[0];
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern.Placeholder#formatLine()
		 */
		@Override
		public String formatLine() {
			return split.length == 1 ? null : split[1];
		}

	}

	record PreformattedArg(String expression, String referenceName,
			String formatLine, SpecificValueFormatter formatter)
			implements Placeholder {

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern.Placeholder#applyFormat(java.lang.Object)
		 */
		@Override
		public String applyFormat(Object value) {
			return formatter.applyFormat(value);
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern.Placeholder#isFormatPresent()
		 */
		@Override
		public boolean isFormatPresent() {
			return true;
		}
	}

	record PatternRecord(String template, String[] fragments, Placeholder[] placeholders) implements PlaceholderPattern {}

	public static final String VALUE = "value";

	private final FormatRegistry formatRegistry;
	private final String[] fragments;
	private final Placeholder[] placeholders;
	private final String template;

	public DefaultTemplatePattern(FormatRegistry formatRegistry, String template, Macros macros) {
		this.template = template;
		this.formatRegistry = formatRegistry;

		List<String> fragments = new ArrayList<>();
		List<Placeholder> placeholders = new ArrayList<>();

		parseTemplateString(template, 0, fragments, placeholders);
		substituteMacros(macros, placeholders);
		resolveFormatters(placeholders);

		this.fragments = fragments.toArray(String[]::new);
		this.placeholders = placeholders.toArray(Placeholder[]::new);
	}

	public DefaultTemplatePattern(String template, Macros macros) {
		this(new DefaultFormats(), template, macros);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern#placeholders()
	 */
	@Override
	public Placeholder[] placeholders() {
		return placeholders;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern#fragments()
	 */
	@Override
	public String[] fragments() {
		return fragments;
	}

	private int parseTemplateString(String str, int start, List<String> frags, List<Placeholder> placeholders) {
		if (str == null || str.isEmpty()) {
			frags.add("");
			return start;
		}

		// Normalize line endings
		str = str.replace("\r\n", "\n");

		int pos = start;
		int length = str.length();
		StringBuilder currentFrag = new StringBuilder();

		while (pos < length) {
			if (pos + 1 < length && str.charAt(pos) == '\\' && str.charAt(pos + 1) == '{') {
				frags.add(currentFrag.toString());
				currentFrag = new StringBuilder();
				pos += 2;

				int braceCount = 1;
				int closeBrace = pos;

				while (braceCount > 0 && closeBrace < length) {
					if (str.charAt(closeBrace) == '{') {
						braceCount++;
					} else if (str.charAt(closeBrace) == '}') {
						braceCount--;
					}
					closeBrace++;
				}

				closeBrace--;

				if (braceCount > 0) {
					throw new IllegalArgumentException("Unterminated argument reference at position " + (pos - 1));
				}

				String argContent = str.substring(pos, closeBrace).trim();
				if (argContent.isEmpty()) {
					argContent = VALUE;
				} else if (argContent.startsWith(":"))
					argContent = VALUE + argContent;

				var split = argContent.split(":", 2);
				if (split.length == 1) {
					split = new String[] {
							split[0],
							DefaultFormats.ANY
					};
				}

				placeholders.add(new SplitArg(argContent, split));
				pos = closeBrace + 1;
			} else {
				currentFrag.append(str.charAt(pos));
				pos++;
			}
		}

		frags.add(currentFrag.toString());
		return pos;
	}

	private boolean resolveFormatters(List<Placeholder> placeholders) {
		if (formatRegistry == null)
			return false;

		/* Map or copy all placeholders to temporary list */
		List<Placeholder> resolvedArgs = placeholders.stream()
				.map(this::resolveFormatters)
				.toList();
		
		boolean modified = !placeholders.equals(resolvedArgs);

		/* No need to do anything if there were no changes */
		if (modified) {
			placeholders.clear();
			placeholders.addAll(resolvedArgs);
		}

		return modified;
	}

	private Placeholder resolveFormatters(Placeholder placeholder) {
		String[] split = placeholder.formatLine().split(":");
		if (split.length == 1)
			return resolveFormatters(placeholder, split[0]);

		SpecificValueFormatter[] chain = new SpecificValueFormatter[split.length];

		for (int i = 0; i < split.length; i++) {
			var c = split[i];

			SpecificValueFormatter fmt = formatRegistry.resolveFormat(c);
			chain[i] = fmt;
		}

		var executeChain = new SpecificValueFormatter() {

			StringBuilder sb = new StringBuilder();

			@Override
			public String applyFormat(Object value) {
				sb.setLength(0);

				for (SpecificValueFormatter formatter : chain)
					sb.append(formatter.applyFormat(value));

				return sb.toString();
			}

		};

		return new PreformattedArg(
				placeholder.expression(),
				placeholder.referenceName(),
				placeholder.formatLine(),
				executeChain);
	}

	private Placeholder resolveFormatters(Placeholder placeholder, String formatComponent) {
		SpecificValueFormatter fmt = formatRegistry.resolveFormat(formatComponent);
		if (fmt == null)
			return placeholder;

		return new PreformattedArg(
				placeholder.expression(),
				placeholder.referenceName(),
				formatComponent,
				fmt);
	}

	/**
	 * @param macros
	 * @param placeholders
	 * @param rawArgs
	 */
	private boolean substituteMacros(Macros macros, List<Placeholder> placeholders) {
		
		Objects.requireNonNull(macros, "macros");

		AtomicInteger replacedCount = new AtomicInteger();
		var tempList = placeholders.stream()
				.map(arg -> {
					var ref = arg.referenceName().trim();
					var fmt = arg.formatLine();

					String newRef = macros.replaceOrDefault(ref, VALUE);
					String newFmt = macros.replaceOrDefault(fmt, DefaultFormats.ANY);

					if (newRef == null && newFmt == null)
						return arg;

					replacedCount.incrementAndGet();

					var newArg = new SplitArg(arg.expression(), new String[] {
							newRef == null ? ref : newRef,
							newFmt == null ? fmt : newFmt
					});

					return newArg;
				})
				.toList();

		if (replacedCount.get() > 0) {
			placeholders.clear();
			placeholders.addAll(tempList);
		}

		return replacedCount.get() > 0;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 5;
		return "PlaceholderPattern ["
				+ "frags=" + Arrays.asList(fragments).subList(0, Math.min(fragments.length, maxLen))
				+ ", placeholders=" + Arrays.asList(placeholders).subList(0, Math.min(placeholders.length, maxLen))
				+ "]";
	}

}