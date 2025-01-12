package com.slytechs.jnet.protocol.api.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.PlaceholderPattern;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.PlaceholderPattern.Placeholder;

public class MetaTemplateFormatter {
	private static final Logger logger = LoggerFactory.getLogger(MetaTemplateFormatter.class);

	public static final String VALUE = "value";

	private FormatRegistry formatRegistry = FormatRegistry.of();

	public MetaTemplateFormatter() {

	}

	public String format(Object cwd, DomainAccessor domain, PlaceholderPattern placeholderPattern) {

		StringBuilder sb = new StringBuilder();
		var frags = placeholderPattern.fragments();
		var args = placeholderPattern.placeholders();

		for (int i = 0; i < frags.length; i++) {
			String frag = frags[i];
			sb.append(frag);

			if (i >= args.length)
				continue;

			try {
				Placeholder placeholder = args[i];
				Object refValue = domain.resolve(placeholder.referenceName(), cwd);

				String formatted = placeholder.applyFormatOrElse(refValue, formatRegistry);

				sb.append(formatted);
			} catch (Throwable e) {
				logger.error("arg=%s".formatted(placeholderPattern.toString()));
				
				throw e;
			}
		}

		return sb.toString();
	}

}