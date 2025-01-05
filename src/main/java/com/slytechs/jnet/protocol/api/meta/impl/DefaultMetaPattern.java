package com.slytechs.jnet.protocol.api.meta.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.slytechs.jnet.protocol.api.meta.DefaultFormatters;
import com.slytechs.jnet.protocol.api.meta.FormatRegistry;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaMacros;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.ValueFormatter.SpecificValueFormatter;

public class DefaultMetaPattern implements MetaPattern {

	record SplitArg(String expression, String[] split) implements Arg {

		@Override
		public boolean isFormatPresent() {
			return split.length == 2;
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern.Arg#referenceName()
		 */
		@Override
		public String referenceName() {
			return split[0];
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern.Arg#formatName()
		 */
		@Override
		public String formatName() {
			return split.length == 1 ? null : split[1];
		}

	}

	record PreformattedArg(String expression, String referenceName, String formatName, SpecificValueFormatter formatter)
			implements Arg {

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern.Arg#applyFormat(java.lang.Object)
		 */
		@Override
		public String applyFormat(Object value) {
			return formatter.applyFormat(value);
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern.Arg#isFormatPresent()
		 */
		@Override
		public boolean isFormatPresent() {
			return true;
		}
	}

	record PatternRecord(String template, String[] fragments, Arg[] args) implements MetaPattern {}

	public static final String VALUE = "value";

	private final FormatRegistry formatRegistry;
	private final String[] fragments;
	private final Arg[] args;
	private final String template;

	public DefaultMetaPattern(FormatRegistry formatRegistry, String template, MetaMacros metaMacros) {
		this.template = template;
		this.formatRegistry = formatRegistry;

		List<String> fragments = new ArrayList<>();
		List<Arg> args = new ArrayList<>();

		parseTemplateString(template, 0, fragments, args);
		substituteMacros(metaMacros, args);
		resolveFormatters(args);

		this.fragments = fragments.toArray(String[]::new);
		this.args = args.toArray(Arg[]::new);
	}

	public DefaultMetaPattern(String template, MetaMacros metaMacros) {
		this(new DefaultFormatters(), template, metaMacros);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern#args()
	 */
	@Override
	public Arg[] args() {
		return args;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern#fragments()
	 */
	@Override
	public String[] fragments() {
		return fragments;
	}

	private int parseTemplateString(String str, int start, List<String> frags, List<Arg> args) {
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
				if (split.length == 1)
					split = new String[] {
							split[0],
							"ANY"
					};

				args.add(new SplitArg(argContent, split));
				pos = closeBrace + 1;
			} else {
				currentFrag.append(str.charAt(pos));
				pos++;
			}
		}

		frags.add(currentFrag.toString());
		return pos;
	}

	private boolean resolveFormatters(List<Arg> args) {
		if (formatRegistry == null)
			return false;

		AtomicInteger count = new AtomicInteger();

		/* Map or copy all args to temporary list */
		List<Arg> resolvedArgs = args.stream()
				.map(arg -> {
					var fmt = formatRegistry.resolveFormat(arg.formatName());
					if (fmt == null)
						return arg;

					return new PreformattedArg(
							arg.expression(),
							arg.referenceName(),
							arg.formatName(),
							fmt);
				})
				.map(arg -> arg)
				.toList();

		/* No need to do anything if there were no changes */
		if (count.get() > 0) {
			args.clear();
			args.addAll(resolvedArgs);
		}

		return count.get() > 0;
	}

	/**
	 * @param metaMacros
	 * @param args
	 * @param rawArgs
	 */
	private boolean substituteMacros(MetaMacros metaMacros, List<Arg> args) {
		AtomicInteger count = new AtomicInteger();
		var tempList = args.stream()
				.map(arg -> {
					var ref = arg.referenceName().trim();
					var fmt = arg.isFormatPresent() ? arg.formatName().trim() : null;

					if (metaMacros.isMacroPresent(ref) || metaMacros.isMacroPresent(fmt))
						count.incrementAndGet();

					ref = metaMacros.resolveIfPresent(ref);
					fmt = fmt == null ? "any" : metaMacros.resolveIfPresent(fmt);

					return new SplitArg(arg.expression(), new String[] {
							ref,
							fmt
					});
				})
				.toList();

		if (count.get() > 0) {
			args.clear();
			args.addAll(tempList);
		}

		return count.get() > 0;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 5;
		return "MetaPattern ["
				+ "frags=" + Arrays.asList(fragments).subList(0, Math.min(fragments.length, maxLen))
				+ ", args=" + Arrays.asList(args).subList(0, Math.min(args.length, maxLen))
				+ "]";
	}

}