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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.slytechs.jnet.jnetruntime.internal.json.JsonObject;

/**
 * The Class ReflectedMethod.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ReflectedMethod extends ReflectedMember {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(ReflectedMethod.class.getPackageName());

	/**
	 * Parses the.
	 *
	 * @param cl         the cl
	 * @param jsonFields the json fields
	 * @return the reflected method[]
	 */
	public static ReflectedMethod[] parse(Class<?> cl, JsonObject jsonFields) {
		var list = new ArrayList<ReflectedMethod>();

		parseMethodsRecursively(cl, list, jsonFields);

		return list.toArray(ReflectedMethod[]::new);
	}

	/**
	 * Parses the methods recursively.
	 *
	 * @param cl         the cl
	 * @param list       the list
	 * @param jsonFields the json fields
	 */
	private static void parseMethodsRecursively(Class<?> cl, List<ReflectedMethod> list, JsonObject jsonFields) {

//		if (cl.getSuperclass() == Object.class)
//			return;
//
//		parseMethodsRecursively(cl.getSuperclass(), list, jsonFields);

		for (Method m : cl.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Meta.class))
				list.add(parseMethod(m, jsonFields));
		}

	}

	/**
	 * Parses the method.
	 *
	 * @param method     the method
	 * @param jsonFields the json fields
	 * @return the reflected method
	 */
	private static ReflectedMethod parseMethod(Method method, JsonObject jsonFields) {
		String name = method.getName();

		JsonObject field = null;
		if (jsonFields != null)
			field = jsonFields.getJsonObject(name);

		var container = MetaInfo.parse(method, name, field);
		return new ReflectedMethod(method, container);
	}

	/** The method. */
	private final Method method;

	/**
	 * Instantiates a new reflected method.
	 *
	 * @param method   the method
	 * @param metaInfo the meta info
	 */
	private ReflectedMethod(Method method, MetaInfo metaInfo) {
		super(metaInfo);
		this.method = method;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Gets the value.
	 *
	 * @param <T>    the generic type
	 * @param target the target
	 * @param args   the args
	 * @return the value
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#getValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(Object target, Object... args) {
		try {
			if (isStatic(method))
				return (T) method.invoke(null, args);
			else
				return (T) method.invoke(target, args);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new MetaException("unable to get reflected method value [%s]"
					.formatted(method.toString()), unwindCauseException(e));
		}
	}

	/**
	 * Sets the value.
	 *
	 * @param target the target
	 * @param args   the args
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#setValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public void setValue(Object target, Object... args) {
		try {
			if (isStatic(method))
				method.invoke(null, args);
			else
				method.invoke(target, args);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new MetaException("unable to set reflected method value [%s]"
					.formatted(method.toString()), unwindCauseException(e));
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
		return method.getReturnType();
	}

	/**
	 * Gets the member.
	 *
	 * @return the member
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#getMember()
	 */
	@Override
	protected Member getMember() {
		return getMethod();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.ReflectedMember#isClassMethod()
	 */
	@Override
	public boolean isClassMethod() {
		return true;
	}
}
