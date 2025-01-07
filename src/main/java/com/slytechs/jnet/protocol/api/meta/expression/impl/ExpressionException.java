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
package com.slytechs.jnet.protocol.api.meta.expression.impl;

/**
 * Exception thrown during expression parsing or evaluation.
 */
class ExpressionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new expression exception with the specified message.
	 *
	 * @param message the error message
	 */
	public ExpressionException(String message) {
		super(message);
	}

	/**
	 * Constructs a new expression exception with the specified message and cause.
	 *
	 * @param message the error message
	 * @param cause   the cause of this exception
	 */
	public ExpressionException(String message, Throwable cause) {
		super(message, cause);
	}
}