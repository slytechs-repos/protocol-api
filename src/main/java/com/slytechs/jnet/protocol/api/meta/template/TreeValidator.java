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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TreeValidator {

	public static class ListValidator {
		private ValidationRule<Object>[] elementRules;
		private int minSize = 0;
		private ValidationRule<List<Object>> customValidation;

		public ListValidator customValidation(ValidationRule<List<Object>> rule) {
			this.customValidation = rule;
			return this;
		}

		@SafeVarargs
		public final ListValidator elementRule(ValidationRule<Object>... rules) {
			this.elementRules = rules;
			return this;
		}

		public ListValidator minSize(int size) {
			this.minSize = size;
			return this;
		}

		public ValidationResult validate(List<?> list) {
			if (list == null) {
				return ValidationResult.failure("List cannot be null");
			}

			if (list.size() < minSize) {
				return ValidationResult.failure("List size must be at least " + minSize);
			}

			List<ValidationResult> results = new ArrayList<>();

			if (elementRules != null) {
				for (int i = 0; i < list.size(); i++) {
					Object element = list.get(i);
					ValidationResult elementResult = validateElement(element);
					if (!elementResult.isValid()) {
						results.add(new ValidationResult(false,
								elementResult.getErrors().stream()
										.map(error -> "Element at index " + ": " + error)
										.collect(Collectors.toList())));
					}
				}
			}

			if (customValidation != null) {
				results.add(customValidation.validate((List<Object>) list));
			}

			return ValidationResult.combine(results);
		}

		private ValidationResult validateElement(Object element) {
			if (elementRules == null || elementRules.length == 0)
				return ValidationResult.success();

			if (elementRules.length == 1)
				return elementRules[0].validate(element);

			List<ValidationResult> results = new ArrayList<>();
			for (var rule : elementRules) {
				var elementResult = rule.validate(element);

				if (!elementResult.isValid()) {
					results.add(new ValidationResult(false,
							elementResult.getErrors().stream()
									.map(error -> "Element at index " + ": " + error)
									.collect(Collectors.toList())));
				}
			}

			// Success if at least 1 rule passed (rules are ORed)
			if (results.size() != elementRules.length)
				return ValidationResult.success();

			// Otherwise report all validated failures
			return ValidationResult.combine(results);
		}
	}

	public static class MapValidator {
		private final Map<String, ValidationRule<Object>> requiredFields = new HashMap<>();
		private final Map<String, ValidationRule<Object>> optionalFields = new HashMap<>();
		private ValidationRule<Map<?, ?>> customValidation;
		private int minSize = 0;

		public MapValidator customValidation(ValidationRule<Map<?, ?>> rule) {
			this.customValidation = rule;
			return this;
		}

		public MapValidator minSize(int size) {
			this.minSize = size;
			return this;
		}

		public MapValidator optionalField(String key, ValidationRule<Object> rule) {
			optionalFields.put(key, rule);
			return this;
		}

		public MapValidator requireField(String key, ValidationRule<Object> rule) {
			requiredFields.put(key, rule);
			return this;
		}

		public ValidationResult validate(Map<?, ?> map) {
			if (map == null) {
				return ValidationResult.failure("Map cannot be null");
			}

			if (map.size() < minSize) {
				return ValidationResult.failure("Map size must be at least " + minSize);
			}

			List<ValidationResult> results = new ArrayList<>();

			// Validate required fields
			for (Map.Entry<String, ValidationRule<Object>> entry : requiredFields.entrySet()) {
				String key = entry.getKey();
				Object value = map.get(key);

				if (value == null) {
					results.add(ValidationResult.failure("Required field '" + key + "' is missing"));
				} else {
					results.add(entry.getValue().validate(value));
				}
			}

			// Validate optional fields if present
			for (Map.Entry<String, ValidationRule<Object>> entry : optionalFields.entrySet()) {
				String key = entry.getKey();
				Object value = map.get(key);

				if (value != null) {
					results.add(entry.getValue().validate(value));
				}
			}

			// Apply custom validation if present
			if (customValidation != null) {
				results.add(customValidation.validate(map));
			}

			return ValidationResult.combine(results);
		}
	}

	public static class ValidationResult {
		public static ValidationResult combine(List<ValidationResult> results) {
			List<String> allErrors = results.stream()
					.filter(r -> !r.isValid())
					.flatMap(r -> r.getErrors().stream())
					.collect(Collectors.toList());

			return new ValidationResult(allErrors.isEmpty(), allErrors);
		}

		public static ValidationResult failure(String error) {
			return new ValidationResult(false, Collections.singletonList(error));
		}

		public static ValidationResult success() {
			return new ValidationResult(true, Collections.emptyList());
		}

		private final boolean valid;

		private final List<String> errors;

		public ValidationResult(boolean valid, List<String> errors) {
			this.valid = valid;
			this.errors = errors;
		}

		public List<String> getErrors() {
			return errors;
		}

		public boolean isFailure() {
			return !valid;
		}

		public boolean isValid() {
			return valid;
		}
	}

	@FunctionalInterface
	public interface ValidationRule<T> {
		ValidationResult validate(T value);
	}

	public static ValidationRule<Object> isBoolean() {
		return value -> value instanceof Boolean
				? ValidationResult.success()
				: ValidationResult.failure("Expected a boolean, but got " + value.getClass().getSimpleName());
	}

	public static <E extends Enum<E>> ValidationRule<Object> isEnum(Class<E> enumClass) {
		return value -> {
			if (!(value instanceof String e))
				return ValidationResult.failure("Expected a string, but got " + value.getClass().getSimpleName());

			var arr = enumClass.getEnumConstants();
			for (var c : arr)
				if (c.name().equalsIgnoreCase(e))
					return ValidationResult.success();

			return ValidationResult.failure("Expected an enum constant of, but got "
					+ value.getClass().getSimpleName());
		};
	}

	public static ValidationRule<Object> isNumber() {
		return value -> value instanceof Number
				? ValidationResult.success()
				: ValidationResult.failure("Expected a number, but got " + value.getClass().getSimpleName());
	}

	public static ValidationRule<Object> isString() {
		return value -> value instanceof String
				? ValidationResult.success()
				: ValidationResult.failure("Expected a string, but got " + value.getClass().getSimpleName());
	}

	// Utility methods for common validations
	public static ValidationRule<Object> isType(Class<?> type) {
		return value -> type.isInstance(value)
				? ValidationResult.success()
				: ValidationResult.failure("Expected type " + type.getSimpleName() +
						", but got " + value.getClass().getSimpleName());
	}

	// Example usage
	public static void main(String[] args) {
		// Create a validator for a nested structure
		MapValidator personValidator = new MapValidator()
				.requireField("name", isString())
				.requireField("age", isNumber())
				.optionalField("address", new ValidationRule<Object>() {
					@Override
					public ValidationResult validate(Object value) {
						if (!(value instanceof Map<?, ?> map)) {
							return ValidationResult.failure("Address must be a map");
						}

						return new MapValidator()
								.requireField("street", isString())
								.requireField("city", isString())
								.optionalField("zipCode", isString())
								.validate(map);
					}
				})
				.optionalField("phoneNumbers", new ValidationRule<Object>() {
					@Override
					public ValidationResult validate(Object value) {
						if (!(value instanceof List)) {
							return ValidationResult.failure("Phone numbers must be a list");
						}

						return new ListValidator()
								.elementRule(isString())
								.minSize(1)
								.validate((List<Object>) value);
					}
				});

		// Test the validator
		Map<String, Object> person = new HashMap<>();
		person.put("name", "John Doe");
		person.put("age", 30);

		Map<String, Object> address = new HashMap<>();
		address.put("street", "123 Main St");
		address.put("city", "New York");
		person.put("address", address);

		List<Object> phoneNumbers = Arrays.asList("123-456-7890", "098-765-4321");
		person.put("phoneNumbers", phoneNumbers);

		ValidationResult result = personValidator.validate(person);
		if (result.isValid()) {
			System.out.println("Validation successful!");
		} else {
			System.out.println("Validation failed:");
			result.getErrors().forEach(System.out::println);
		}
	}
}