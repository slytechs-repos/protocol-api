package com.slytechs.jnet.protocol.api.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern.Arg;

public class MetaTemplateFormatter {
	private static final Logger logger = LoggerFactory.getLogger(MetaTemplateFormatter.class);

	public static final String VALUE = "value";

	private FormatRegistry formatRegistry = FormatRegistry.valueOfString();

	public MetaTemplateFormatter() {

	}

	public String format(Object cwd, DomainAccessor domain, MetaPattern metaPattern) {

		StringBuilder sb = new StringBuilder();
		var frags = metaPattern.fragments();
		var args = metaPattern.args();

		for (int i = 0; i < frags.length; i++) {
			String frag = frags[i];
			sb.append(frag);

			if (i >= args.length)
				continue;

			try {
				Arg arg = args[i];
				Object refValue = domain.resolve(arg.referenceName(), cwd);

				String formatted = arg.applyFormatOrElse(refValue, formatRegistry);

				sb.append(formatted);
			} catch (Throwable e) {
				logger.error("arg=%s".formatted(metaPattern.toString()));
				
				throw e;
			}
		}

		return sb.toString();
	}

}