/*
 * Sly Technologies Free License
 * 
 * Copyright 2025 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.api.meta.template.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.slytechs.jnet.platform.api.common.NotFound;
import com.slytechs.jnet.protocol.api.meta.template.ResourceTemplate;
import com.slytechs.jnet.protocol.api.meta.template.Template;

/**
 * A resource reader that loads templates based on dialect and locale, with
 * support for template selection and resource resolution.
 */
public class ResourceLocator {

	/**
	 * Supported resource dialects.
	 */
	public enum ResourceDialect {
		WIRESHARK,
		TCPDUMP,
		XML,
		DEFAULT;

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	/**
	 * Supported resource file types.
	 */
	public enum ResourceType {
		YAML("yaml"),
		JSON("json"),
		XML("xml");

		private final String extension;

		ResourceType(String extension) {
			this.extension = extension;
		}

		public String extension() {
			return extension;
		}

		public static ResourceType fromExtension(String ext) {
			for (ResourceType type : values()) {
				if (type.extension.equals(ext)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Unsupported file extension: " + ext);
		}
	}

	private static final Pattern RESOURCE_PATTERN = Pattern.compile(
			"^(.*?)(?:-(\\w+))?(?:_(\\w+))?\\.(\\w+)(?:#(.+))?$");

	private final Function<String, InputStream> resourceLoader;
	private final Locale defaultLocale;
	private final ResourceDialect defaultDialect;
	private final ResourceType defaultType;

	/**
	 * Creates a new ResourceLocator using defaults.
	 */
	public ResourceLocator(Function<String, InputStream> resourceLoader) {
		this(resourceLoader,
				Locale.getDefault(),
				ResourceDialect.DEFAULT,
				ResourceType.YAML);
	}

	/**
	 * Creates a new ResourceLocator using defaults.
	 */
	public ResourceLocator() {
		this(name -> Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(name),
				Locale.getDefault(),
				ResourceDialect.DEFAULT,
				ResourceType.YAML);
	}

	/**
	 * Creates a new ResourceLocator with specific configuration.
	 */
	public ResourceLocator(Function<String, InputStream> resourceLoader, Locale defaultLocale,
			ResourceDialect defaultDialect, ResourceType defaultType) {
		this.resourceLoader = resourceLoader;
		this.defaultLocale = defaultLocale;
		this.defaultDialect = defaultDialect;
		this.defaultType = defaultType;
	}

	/**
	 * Resolves and reads a template using the best matching resource.
	 *
	 * @param resourcePath the base resource path
	 * @return the resolved template
	 * @throws IOException if an I/O error occurs
	 * @throws NotFound    if no matching resource is found
	 */
	public Template resolveTemplate(String resourcePath) throws IOException, NotFound {
		ResourceInfo info = parseResourcePath(resourcePath);
		String resolvedPath = resolveResourcePath(info);

		if (resolvedPath == null) {
			throw new NotFound("No matching resource found for: " + resourcePath);
		}

		try (InputStream is = resourceLoader.apply(resolvedPath)) {
			if (is == null) {
				throw new NotFound("Resource not found: " + resolvedPath);
			}

			ResourceTemplate resourceTemplate = TemplateReader.readResourceTemplate(is);

			if (info.templateName != null) {
				return findTemplateByName(resourceTemplate, info.templateName);
			} else {
				return resourceTemplate.templates().get(0);
			}
		}
	}

	/**
	 * Resolves and reads all templates from the best matching resource.
	 */
	public ResourceTemplate resolveResourceTemplate(String resourcePath) throws IOException, NotFound {
		ResourceInfo info = parseResourcePath(resourcePath);
		String resolvedPath = resolveResourcePath(info);

		if (resolvedPath == null) {
			throw new NotFound("No matching resource found for: " + resourcePath);
		}

		try (InputStream is = resourceLoader.apply(resolvedPath)) {
			if (is == null) {
				throw new NotFound("Resource not found: " + resolvedPath);
			}

			return TemplateReader.readResourceTemplate(is);
		}
	}

	/**
	 * Generates a list of candidate resource paths in order of preference.
	 */
	private List<String> generateCandidatePaths(ResourceInfo info) {
		List<String> candidates = new ArrayList<>();
		Locale locale = info.locale != null ? new Locale(info.locale) : defaultLocale;
		ResourceDialect dialect = info.dialect != null ? info.dialect : defaultDialect;

		// Add candidates with specified/default dialect
		addLocaleCandidates(candidates, info.basePath, dialect, locale, info.type);

		// If not using default dialect, add candidates with default dialect
		if (dialect != defaultDialect) {
			addLocaleCandidates(candidates, info.basePath, defaultDialect, locale, info.type);
		}

		// Add base resource without dialect or locale
		candidates.add(info.basePath + "." + info.type.extension());

		return candidates;
	}

	private void addLocaleCandidates(List<String> candidates, String basePath,
			ResourceDialect dialect, Locale locale, ResourceType type) {
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		String extension = type.extension();

		// Build locale chain from most specific to least specific
		if (!variant.isEmpty()) {
			candidates.add(String.format("%s-%s_%s_%s_%s.%s",
					basePath, dialect, language, country, variant, extension));
		}

		if (!country.isEmpty()) {
			candidates.add(String.format("%s-%s_%s_%s.%s",
					basePath, dialect, language, country, extension));
		}

		candidates.add(String.format("%s-%s_%s.%s",
				basePath, dialect, language, extension));

		candidates.add(String.format("%s-%s.%s",
				basePath, dialect, extension));
	}

	/**
	 * Resolves the best matching resource path from candidates.
	 */
	private String resolveResourcePath(ResourceInfo info) {
		List<String> candidates = generateCandidatePaths(info);

		for (String candidate : candidates) {
			if (resourceLoader.apply(candidate) != null) {
				return candidate;
			}
		}

		return null;
	}

	private Template findTemplateByName(ResourceTemplate resourceTemplate, String templateName) throws NotFound {
		return resourceTemplate.templates().stream()
				.filter(t -> t.name().equals(templateName))
				.findFirst()
				.orElseThrow(() -> new NotFound("Template not found: " + templateName));
	}

	private ResourceInfo parseResourcePath(String resourcePath) {
		var matcher = RESOURCE_PATTERN.matcher(resourcePath);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid resource path format: " + resourcePath);
		}

		return new ResourceInfo(
				matcher.group(1), // basePath
				Optional.ofNullable(matcher.group(2))// dialect
						.map(String::toUpperCase)
						.map(ResourceDialect::valueOf)
						.orElse(null),
				matcher.group(3), // locale
				ResourceType.fromExtension(matcher.group(4)), // type
				matcher.group(5) // templateName
		);
	}

	private record ResourceInfo(
			String basePath,
			ResourceDialect dialect,
			String locale,
			ResourceType type,
			String templateName) {

	}
}