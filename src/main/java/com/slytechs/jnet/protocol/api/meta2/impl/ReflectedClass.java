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
package com.slytechs.jnet.protocol.api.meta.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.slytechs.jnet.platform.api.util.json.JsonException;
import com.slytechs.jnet.platform.api.util.json.JsonObject;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * The Class ReflectedClass.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class ReflectedClass extends ReflectedComponent {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(ReflectedClass.class.getPackageName());

	private static final Function<Class<?>, ReflectedClass> STORAGE = cl -> GlobalContext.compute(cl, ReflectedClass::parse);

	public static ReflectedClass parse(Class<?> cl) {

		ReflectedClass sup = getReflectedParentClass(cl);

		if (sup == null)
			return parseClassNoMerge(cl);
		else
			return parseClassAndMerge(cl, sup);
	}

	private static ReflectedClass getReflectedParentClass(Class<?> cl) {
		Class<?> supCl = cl.getSuperclass();
		boolean isMetaClass = (supCl != null)
				&& (supCl != Object.class)
				&& (supCl.isAnnotationPresent(Meta.class) || supCl.isAnnotationPresent(MetaResource.class));

		if (!isMetaClass)
			return null;

		return STORAGE.apply(supCl);
	}

	public static ReflectedClass parseClassAndMerge(Class<?> cl, ReflectedClass parentReflectedClass) {

		ReflectedClass unmerged = parseClassNoMerge(cl);

		return mergeReflectedClasses(parentReflectedClass, unmerged, unmerged.getMetaType(MetaInfo.class));
	}

	public static ReflectedClass mergeReflectedClasses(ReflectedClass baseClass, ReflectedClass subClass, MetaInfo metaInfo) {
		Map<String, ReflectedMember> rm1 = baseClass.fieldsMap;
		Map<String, ReflectedMember> rm2 = subClass.fieldsMap;

		/* Populate with subClass fields first */
		Map<String, ReflectedMember> combined = new HashMap<>(rm2);

		/* Now add any missing baseClass fields */
		for (String key : rm1.keySet())
			combined.putIfAbsent(key, rm1.get(key));

		ReflectedMethod[] methods = combined.values().stream()
				.filter(ReflectedMember::isClassMethod)
				.toArray(ReflectedMethod[]::new);

		ReflectedField[] fields = combined.values().stream()
				.filter(ReflectedMember::isClassField)
				.toArray(ReflectedField[]::new);

		return new ReflectedClass(subClass.classType, metaInfo, methods, fields);
	}

	/**
	 * Parses the.
	 *
	 * @param cl the cl
	 * @return the reflected class
	 */
	public static ReflectedClass parseClassNoMerge(Class<?> cl) {
		JsonObject jsonConf = null;
		JsonObject jsonFields = null;

		MetaResource res = cl.getAnnotation(MetaResource.class);
		if (res != null) {
			try {
				jsonConf = readMetaResource(res.value());
				jsonFields = jsonConf.getJsonObject("field");

			} catch (JsonException e) {
				LOGGER.log(Level.WARNING, "unable to load meta resource [%s]"
						.formatted(res.value()), e);
			}
		}

		String name = cl.getSimpleName();
		var metaInfo = MetaInfo.parse(cl, name, jsonConf);

		ReflectedMethod[] methods = ReflectedMethod.parse(cl, jsonFields);
		ReflectedField[] fields = ReflectedField.parse(cl, jsonFields);

		return new ReflectedClass(cl, metaInfo, methods, fields);
	}

	/**
	 * Read meta resource.
	 *
	 * @param resourceName the resource name
	 * @return the json object
	 * @throws JsonException the json exception
	 */
	public static JsonObject readMetaResource(String resourceName) throws JsonException {
		JsonObject conf = new MetaResourceShortformReader(resourceName)
				.toJsonObj();

		if (conf == null)
			throw new IllegalStateException("meta resource not found [%s]".formatted(resourceName));

		return conf;
	}

	/**
	 * To sorted array.
	 *
	 * @param methods the methods
	 * @param fields  the fields
	 * @return the reflected member[]
	 */
	private static ReflectedMember[] toSortedUniqueArray(ReflectedMethod[] methods, ReflectedField[] fields) {
		Set<String> dups = new HashSet<>();

		List<ReflectedMember> list = new ArrayList<ReflectedMember>();
		Arrays.stream(methods)
				.filter(m -> !dups.contains(m.name()))
				.peek(m -> dups.add(m.name()))
				.forEach(list::add);

		Arrays.stream(fields)
				.filter(m -> !dups.contains(m.name()))
				.peek(m -> dups.add(m.name()))
				.forEach(list::add);

		Collections.sort(list);

		return list.toArray(ReflectedMember[]::new);
	}

	/** The class type. */
	private final Class<?> classType;

	/** The member methods. */
	private final ReflectedMethod[] memberMethods;

	/** The member fields. */
	private final ReflectedField[] memberFields;

	/** The fields array. */
	private final ReflectedMember[] memberArray;

	/** The fields map. */
	private final Map<String, ReflectedMember> fieldsMap;

	/**
	 * Instantiates a new reflected class.
	 *
	 * @param cl       the cl
	 * @param metaInfo the meta info
	 * @param methods  the methods
	 * @param fields   the fields
	 */
	private ReflectedClass(Class<?> cl, MetaInfo metaInfo,
			ReflectedMethod[] methods,
			ReflectedField[] fields) {
		super(metaInfo);
		this.classType = cl;
		this.memberMethods = methods;
		this.memberFields = fields;
		this.memberArray = toSortedUniqueArray(methods, fields);
		this.fieldsMap = Arrays.stream(this.memberArray)
				.collect(Collectors.toMap(ReflectedMember::name, v -> v));
	}

	/**
	 * Gets the class type.
	 *
	 * @return the classType
	 */
	public Class<?> getClassType() {
		return classType;
	}

	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public ReflectedMember[] getFields() {
		return memberArray;
	}

	/**
	 * Gets the member fields.
	 *
	 * @return the member fields
	 */
	private ReflectedField[] getMemberFields() {
		return memberFields;
	}

	/**
	 * Gets the member methods.
	 *
	 * @return the member methods
	 */
	private ReflectedMethod[] getMemberMethods() {
		return memberMethods;
	}

	/**
	 * Gets the field.
	 *
	 * @param name the name
	 * @return the field
	 */
	public ReflectedMember getField(String name) {
		return fieldsMap.get(name);
	}
}
