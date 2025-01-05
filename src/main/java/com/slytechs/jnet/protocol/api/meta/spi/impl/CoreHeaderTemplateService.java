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

import java.io.IOException;

import com.slytechs.jnet.protocol.api.meta.MetaTemplate.ProtocolTemplate;
import com.slytechs.jnet.protocol.api.meta.impl.TemplateReader;
import com.slytechs.jnet.protocol.api.meta.impl.YamlTemplateReader;
import com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class CoreHeaderTemplateService implements HeaderTemplateService {
	private TemplateReader parser = new YamlTemplateReader();

	/**
	 * 
	 */
	public CoreHeaderTemplateService() {}

	/**
	 * @throws IOException
	 * @see com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService#loadHeaderTemplate(java.lang.String)
	 */
	@Override
	public ProtocolTemplate loadHeaderTemplate(String resource, String name) {
		try {
			return parser.parseResource(resource, name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
