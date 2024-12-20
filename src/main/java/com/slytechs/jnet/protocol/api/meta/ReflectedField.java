/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.api.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import com.slytechs.jnet.platform.api.internal.json.JsonObject;

/**
 * The Class ReflectedField.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ReflectedField extends ReflectedMember {

	/**
	 * Parses the.
	 *
	 * @param cl         the cl
	 * @param jsonFields the json fields
	 * @return the reflected field[]
	 */
	public static ReflectedField[] parse(Class<?> cl, JsonObject jsonFields) {
		var list = new ArrayList<ReflectedField>();

		parseFieldsRecursively(cl, list, jsonFields);

		return list.toArray(ReflectedField[]::new);
	}

	/**
	 * Parses the fields recursively.
	 *
	 * @param cl          the cl
	 * @param list        the list
	 * @param jsonFields  the json fields
	 */
	public static void parseFieldsRecursively(Class<?> cl, List<ReflectedField> list, JsonObject jsonFields) {
		if (cl.getSuperclass() == Object.class)
			return;

		parseFieldsRecursively(cl.getSuperclass(), list, jsonFields);

		for (Field f : cl.getDeclaredFields()) {
			if (f.isAnnotationPresent(Meta.class))
				list.add(parseField(f, jsonFields));
		}

	}

	/**
	 * Parses the field.
	 *
	 * @param field       the field
	 * @param jsonFields  the json fields
	 * @return the reflected field
	 */
	private static ReflectedField parseField(Field field, JsonObject jsonFields) {
		String name = field.getName();
		var container = MetaInfo.parse(field, name, jsonFields);

		return new ReflectedField(field, container);
	}

	/** The field. */
	private final Field field;

	/**
	 * Instantiates a new reflected field.
	 *
	 * @param field         the field
	 * @param metaContainer the meta container
	 */
	private ReflectedField(Field field, MetaInfo metaContainer) {
		super(metaContainer);
		this.field = field;
	}

	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Gets the value.
	 *
	 * @param <T>    the generic type
	 * @param target the target
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(Object target) {

		try {
			if (isStatic(field))
				return (T) field.get(null);
			else
				return (T) field.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new MetaException("unable to get reflected field value [%s]".formatted(field.toString()), e);
		}
	}

	/**
	 * Sets the value.
	 *
	 * @param target   the target
	 * @param newValue the new value
	 */
	public void setValue(Object target, Object newValue) {
		try {
			if (isStatic(field))
				field.set(null, newValue);
			else
				field.set(target, newValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new MetaException("unable to set reflected field value [%s] of '%s'".formatted(field.toString(),
					String.valueOf(newValue)), e);
		}
	}

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return field.getType();
	}

	/**
	 * Gets the value.
	 *
	 * @param <T>    the generic type
	 * @param target the target
	 * @param args   the args
	 * @return the value
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedComponent#getValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public <T> T getValue(Object target, Object... args) {
		return getValue(target);
	}

	/**
	 * Sets the value.
	 *
	 * @param target the target
	 * @param args   the args
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedComponent#setValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public void setValue(Object target, Object... args) {
		setValue(target, args[0]);
	}

	/**
	 * Gets the member.
	 *
	 * @return the member
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#getMember()
	 */
	@Override
	protected Member getMember() {
		return getField();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#isClassMethod()
	 */
	@Override
	public boolean isClassMethod() {
		return false;
	}
}
