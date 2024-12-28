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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly;

/**
 * Enumeration of IP fragment reassembly event types.
 */
public enum IpfEventType {

	/**
	 * Fragment has been accepted for reassembly.
	 */
	FRAGMENT_ACCEPTED(Category.INFO),

	/**
	 * New reassembly process has started.
	 */
	REASSEMBLY_STARTED(Category.INFO),

	/**
	 * Reassembly has completed successfully.
	 */
	REASSEMBLY_COMPLETE(Category.INFO),

	/**
	 * Duplicate fragment detected.
	 */
	DUPLICATE_FRAGMENT(Category.WARNING),

	/**
	 * Overlapping fragment detected.
	 */
	OVERLAPPING_FRAGMENT(Category.WARNING),

	/**
	 * Out-of-order fragment received.
	 */
	OUT_OF_ORDER_FRAGMENT(Category.WARNING),

	/**
	 * Gap detected in fragment sequence.
	 */
	FRAGMENT_GAP_DETECTED(Category.WARNING),

	/**
	 * Reassembly timed out.
	 */
	REASSEMBLY_TIMEOUT(Category.WARNING),

	/**
	 * Invalid fragment received.
	 */
	ERROR_INVALID_FRAGMENT(Category.ERROR),

	/**
	 * Invalid fragment length.
	 */
	ERROR_INVALID_LENGTH(Category.ERROR),

	/**
	 * Invalid fragment offset.
	 */
	ERROR_INVALID_OFFSET(Category.ERROR),

	/**
	 * Fragment exceeds maximum allowed size.
	 */
	ERROR_SIZE_EXCEEDED(Category.ERROR),

	/**
	 * Reassembly table is full.
	 */
	ERROR_TABLE_FULL(Category.ERROR),

	/**
	 * Memory limit reached.
	 */
	ERROR_MEMORY_LIMIT(Category.ERROR);

	/**
	 * Event category for grouping and filtering.
	 */
	public enum Category {
		/**
		 * Informational events indicating normal operation.
		 */
		INFO,

		/**
		 * Warning events indicating potential issues.
		 */
		WARNING,

		/**
		 * Error events indicating operation failures.
		 */
		ERROR
	}

	private final Category category;

	/**
	 * Constructs a new event type with specified category.
	 *
	 * @param category the event category
	 */
	IpfEventType(Category category) {
		this.category = category;
	}

	/**
	 * Gets the category of this event type.
	 *
	 * @return the event category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Checks if this is an informational event.
	 *
	 * @return true if this is an info event
	 */
	public boolean isInfo() {
		return category == Category.INFO;
	}

	/**
	 * Checks if this is a warning event.
	 *
	 * @return true if this is a warning event
	 */
	public boolean isWarning() {
		return category == Category.WARNING;
	}

	/**
	 * Checks if this is an error event.
	 *
	 * @return true if this is an error event
	 */
	public boolean isError() {
		return category == Category.ERROR;
	}
}
