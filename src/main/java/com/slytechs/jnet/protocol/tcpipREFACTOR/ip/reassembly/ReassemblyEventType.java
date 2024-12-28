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
 * @author Mark Bednarczyk
 *
 */
public enum ReassemblyEventType {
	// Success events
	REASSEMBLY_COMPLETE,
	FRAGMENT_RECEIVED,

	// Fragment issues
	DUPLICATE_FRAGMENT,
	OVERLAPPING_FRAGMENT,
	OUT_OF_ORDER_FRAGMENT,
	FRAGMENT_GAP_DETECTED,

	// Resource events
	TABLE_FULL,
	MEMORY_LIMIT_REACHED,
	FRAGMENT_LIMIT_REACHED,

	// Error events
	ERROR_INVALID_FRAGMENT,
	ERROR_INVALID_LENGTH,
	ERROR_INVALID_OFFSET,
	ERROR_FRAGMENT_TOO_LARGE,
	ERROR_UNSUPPORTED_FRAGMENT,

	// Timeout events
	TIMEOUT_INCOMPLETE,
	TIMEOUT_NO_LAST_FRAGMENT,
	TIMEOUT_MISSING_FRAGMENTS,

	// Cleanup events
	CLEANUP_FORCED,
	CLEANUP_TABLE_FULL,
	CLEANUP_MEMORY_LIMIT
}
