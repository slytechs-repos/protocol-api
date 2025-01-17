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
package com.slytechs.jnet.protocol.api.meta.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.template.Defaults.Align;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class ResourceTemplateValidator extends TreeValidator {

	private final MapValidator resourceValidator;

	/**
	 * 
	 */
	public ResourceTemplateValidator() {
		resourceValidator = new MapValidator()
				.requireField("Overview", validateOverview())
				.requireField("Defaults", validateDefaults())
				.requireField("Macros", validateMacros())
				.requireField("Imports", validateImports())
				.requireField("Templates", validateTemplates());

	}

	private ValidationRule<Object> validateTemplates() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof List<?> list))
					return ValidationResult.failure("Templates must be a list");

				return new ListValidator()
						.elementRule(validateTemplate())
						.minSize(1)
						.validate(list);
			}
		};
	}

	private ValidationRule<Object> validateTemplate() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("HeaderTemplate must be a map");

				return new MapValidator()
						.requireField("Header", isString())
						.optionalField("class", isString())
						.optionalField("Defaults", validateDefaults())
						.optionalField("Imports", validateImports())
						.requireField("Details", validateDetails())
						.validate(map);
			}
		};

	}

	private ValidationRule<Object> validateDetails() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof List<?> list))
					return ValidationResult.failure("Details must be a list");

				return new ListValidator()
						.minSize(1)
						.elementRule(validateDetailTemplate())
						.validate(list);
			}
		};
	}

	private ValidationRule<Object> validateDetailTemplate() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("DetailTemplate must be a map");

				return new MapValidator()
						.requireField("detail", isEnum(Detail.class))
						.optionalField("summary", isString())
						.optionalField("Defaults", validateDefaults())
						.requireField("Items", validateItems())
						.validate(map);
			}
		};

	}

	private ValidationRule<Object> validateItems() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof List<?> list))
					return ValidationResult.failure("Items must be a list");

				return new ListValidator()
						.minSize(1)
						// An item can be a Field or a Header or an info or a string
						.elementRule(
								validateFieldTemplate(),
								validateHeaderTemplate(),
								validateInfoTemplate(),
								isString())
						.validate(list);
			}
		};
	}

	private ValidationRule<Object> validateFieldTemplate() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("FieldTemplate must be a map");

				return new MapValidator()
						.requireField("Field", isString())
						.optionalField("template", isString())
						.optionalField("label", isString())
						.optionalField("Defaults", validateDefaults())
						.optionalField("tags", validateTags())
						.validate(map);
			}
		};

	}

	private ValidationRule<Object> validateHeaderTemplate() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("HeaderTemplate must be a map");

				return new MapValidator()
						.requireField("Header", isString())
						.optionalField("summary", isString())
						.optionalField("repeatable", isBoolean())
						.optionalField("Defaults", validateDefaults())
						.validate(map);
			}
		};

	}

	private ValidationRule<Object> validateInfoTemplate() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("Info must be a map");

				return new MapValidator()
						.requireField("template", isString())
						.optionalField("label", isString())
						.validate(map);
			}
		};

	}

	private ValidationRule<Object> validateTags() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof List<?> list))
					return ValidationResult.failure("Tags must be a list");

				return new ListValidator()
						.minSize(1)
						.elementRule(isString())
						.validate(list);
			}
		};
	}

	private ValidationRule<Object> validateOverview() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("Overview must be a map");

				return new MapValidator()
						.requireField("description", isString())
						.optionalField("name", isString())
						.optionalField("version", isString())
						.optionalField("Defaults", validateDefaults())
						.optionalField("Imports", validateImports())
						.optionalField("Macros", validateMacros())
						.validate(map);
			}
		};
	}

	private ValidationRule<Object> validateDefaults() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("Defaults must be a map");

				return new MapValidator()
						.optionalField("indent", isNumber())
						.optionalField("width", isNumber())
						.optionalField("align", isEnum(Align.class))
						.validate(map);
			}
		};
	}

	private ValidationRule<Object> validateImports() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof List<?> list))
					return ValidationResult.failure("Imports must be a list");

				return new ListValidator()
						.minSize(1)
						// Either an import statement or direct string
						.elementRule(isType(String.class), validateImport())
						.validate(list);
			}
		};
	}

	private ValidationRule<Object> validateImport() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("Import must be a map");

				return new MapValidator()
						.requireField("import", isString())
						.validate(map);
			}
		};
	}

	private ValidationResult validateMacroMap(Map<?, ?> map) {
		List<ValidationResult> results = new ArrayList<>();

		for (var e : map.entrySet()) {
			String key = (String) e.getKey();
			if (!key.startsWith("$"))
				results.add(ValidationResult.failure("macro %s must start with '$'".formatted(key)));

			else if (!(e.getValue() instanceof String))
				results.add(ValidationResult.failure("macro %s value must be of type String"
						.formatted(key)));
		}

		return ValidationResult.combine(results);
	}

	private ValidationRule<Object> validateMacros() {
		return new ValidationRule<Object>() {
			@Override
			public ValidationResult validate(Object value) {
				if (!(value instanceof Map<?, ?> map))
					return ValidationResult.failure("Macros must be a map");

				return new MapValidator()
						.minSize(1)
						.customValidation(m -> validateMacroMap(m))
						.validate(map);
			}
		};

	}

	public ValidationResult validate(Map<String, Object> root) {
		return resourceValidator.validate(root);
	}
}
