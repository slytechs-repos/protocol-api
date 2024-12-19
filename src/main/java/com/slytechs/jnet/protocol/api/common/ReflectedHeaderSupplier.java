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
package com.slytechs.jnet.protocol.api.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The Class ReflectedHeaderSupplier.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ReflectedHeaderSupplier implements HeaderSupplier {

	/** The module name. */
	private final String moduleName;
	
	/** The class name. */
	private final String className;
	
	/** The constructor. */
	private Constructor<? extends Header> constructor;

	/**
	 * New header instance.
	 *
	 * @return the header
	 * @see com.slytechs.jnet.protocol.api.common.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		try {
			return getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw unableToAllocateException(e);
		}
	}

	/**
	 * Gets the constructor.
	 *
	 * @return the constructor
	 */
	@SuppressWarnings("unchecked")
	private Constructor<? extends Header> getConstructor() {

		if (constructor == null) {
			Module module = ModuleLayer.boot()
					.findModule(moduleName)
					.orElseThrow(this::unableToAllocateException);

			try {
				this.constructor = (Constructor<? extends Header>) Class
						.forName(module, className)
						.getConstructor();
			} catch (NoSuchMethodException | SecurityException e) {
				throw unableToAllocateException(e);
			}
		}

		return constructor;
	}

	/**
	 * Unable to allocate exception.
	 *
	 * @param e the e
	 * @return the illegal state exception
	 * @throws IllegalStateException the illegal state exception
	 */
	private IllegalStateException unableToAllocateException(Throwable e) throws IllegalStateException {
		return new IllegalStateException("unable to allocate header [%s in module %s]"
				.formatted(className, moduleName), e);
	}

	/**
	 * Unable to allocate exception.
	 *
	 * @return the illegal state exception
	 * @throws IllegalStateException the illegal state exception
	 */
	private IllegalStateException unableToAllocateException() throws IllegalStateException {
		return unableToAllocateException(null);
	}

	/**
	 * Instantiates a new reflected header supplier.
	 *
	 * @param moduleName the module name
	 * @param className  the class name
	 */
	public ReflectedHeaderSupplier(String moduleName, String className) {
		this.moduleName = moduleName;
		this.className = className;
	}

}
