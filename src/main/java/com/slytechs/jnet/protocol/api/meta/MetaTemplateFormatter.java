package com.slytechs.jnet.protocol.api.meta;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern.Arg;

public class MetaTemplateFormatter {

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

			Arg arg = args[i];
			Object refValue = domain.resolve(arg.referenceName(), cwd);

			String formatted = arg.applyFormatOrElse(refValue, formatRegistry);

			sb.append(formatted);

		}

		return sb.toString();
	}

}