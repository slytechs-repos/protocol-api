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

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Interface Meta.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@Retention(RUNTIME)
@Target({
		TYPE,
		METHOD,
		FIELD
})
public @interface Meta {

	/**
	 * The Enum MetaType.
	 */
	public enum MetaType {

		/** A displayble field. */
		FIELD,

		/**
		 * Attributes do not show up as fields when displayed, but can be accessed like
		 * any field from any meta element.
		 */
		ATTRIBUTE,

	}

	public enum Formatter {
		NONE,
		HEX_LOWERCASE_0x
	}

	Formatter formatter() default Formatter.NONE;

	/**
	 * Ordinal.
	 *
	 * @return the int
	 */
	int ordinal() default Integer.MAX_VALUE;

	/**
	 * Abbr.
	 *
	 * @return the string
	 */
	String abbr() default "";

	/**
	 * Name.
	 *
	 * @return the string
	 */
	String name() default "";

	/**
	 * Note.
	 *
	 * @return the string
	 */
	String note() default "";

	/**
	 * Value.
	 *
	 * @return the meta type
	 */
	MetaType value() default MetaType.FIELD;

	int offset() default -1;

	String offsetRef() default "offset";

	int length() default -1;

	String lengthRef() default "length";
}
