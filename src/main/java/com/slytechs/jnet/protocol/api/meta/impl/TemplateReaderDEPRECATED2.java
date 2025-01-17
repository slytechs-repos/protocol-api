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
package com.slytechs.jnet.protocol.api.meta.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.slytechs.jnet.protocol.api.meta.template.Defaults;
import com.slytechs.jnet.protocol.api.meta.template.HeaderTemplate;

public interface TemplateReaderDEPRECATED2 {

	/**
	 * Parse header template from a classpath resource
	 * 
	 * @param resourcePath path to the resource (e.g., "/templates/ethernet.yml")
	 * @return parsed HeaderTemplate
	 * @throws IOException if resource cannot be read
	 */
	HeaderTemplate parseResource(String resourcePath) throws IOException;

	default HeaderTemplate parseResource(String resourcePath, String name) throws IOException {
		if (name == null)
			return parseResource(resourcePath);

		var map = parseAllResources(resourcePath);

		return map.get(name);
	}

	Map<String, HeaderTemplate> parseAllResources(String resourcePath) throws IOException;

	/**
	 * Parse header template from a Reader
	 * @throws IOException 
	 */
	HeaderTemplate parseHeader(Reader reader) throws IOException;

	Map<String, HeaderTemplate> parseAllHeaders(Reader reader);

	/**
	 * Parse header template from a String
	 */
	HeaderTemplate parseHeader(String content);

	Defaults defaults();
	
	void setDefaults(Defaults newDefaults);
}