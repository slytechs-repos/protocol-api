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

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver;

/**
 * Resolves field values to human readable representations. For example IP
 * addresses are can be resolved to host names, etc..
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Repeatable(Resolvers.class)
@Retention(RUNTIME)
@Target({
		METHOD,
		FIELD
})
public @interface Resolver {

	

	/**
	 * Value.
	 *
	 * @return the resolver type
	 */
	String value() default "NONE";

	/**
	 * Resolver class.
	 *
	 * @return A value resolver compatible class which can be instantiated
	 */
	Class<? extends ValueResolver> resolverClass() default ValueResolver.class;

	/**
	 * If resolver does not exist or is unable to resolve the value, default to
	 * formatted value output {@link MetaField#getFormatted}.
	 *
	 * @return true, if successful
	 */
	boolean defaultToFormatted() default true;

	/**
	 * Default value.
	 *
	 * @return the string
	 */
	String defaultValue() default "";
}
