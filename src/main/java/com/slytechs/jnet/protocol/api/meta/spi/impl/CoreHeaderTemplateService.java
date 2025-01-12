/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.jnet.protocol.api.meta.spi.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slytechs.jnet.protocol.api.meta.impl.YamlTemplateReaderDEPRECATED;
import com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Template;
import com.slytechs.jnet.protocol.api.meta.template.TemplateReader;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class CoreHeaderTemplateService implements HeaderTemplateService {
	private static final Logger logger = LoggerFactory.getLogger(YamlTemplateReaderDEPRECATED.class.getSimpleName());

	private TemplateReader reader = new TemplateReader();

	public CoreHeaderTemplateService() {}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService#loadHeaderTemplate(java.lang.String)
	 */
	@Override
	public Template loadHeaderTemplate(String resource, String name) {

		try {
			var in = CoreHeaderTemplateService.class.getResourceAsStream(resource);
			if (in == null)
				throw new FileNotFoundException(resource);

			Template protocol = TemplateReader.parseYamlTemplate(in);

			return protocol;
		} catch (Throwable e) {
			logger.error("unable to load {} at resource {}, error={}", name, resource, e.getMessage());
			return null;
		}
	}

}
