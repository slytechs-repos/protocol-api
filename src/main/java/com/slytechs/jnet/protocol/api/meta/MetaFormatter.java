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
package com.slytechs.jnet.protocol.api.meta;

/**
 * Interface defining the core formatting operations for protocol data.
 */
public interface MetaFormatter {
    /**
     * Format a summary line for the protocol data.
     * @param model The data model to format
     * @return Formatted summary string
     */
    String formatSummary(Object model);
    
    /**
     * Format a field within the protocol data.
     * @param name Field name
     * @param value Field value
     * @return Formatted field string
     */
    String formatField(String name, Object value);
    
    /**
     * Format an item entry in the protocol data.
     * @param name Item name
     * @param value Item value
     * @return Formatted item string
     */
    String formatItem(String name, Object value);
    
    /**
     * Format branch information for hierarchical data.
     * @param name Branch name
     * @param value Branch value
     * @return Formatted branch string
     */
    String formatBranch(String name, Object value);
    
    /**
     * Format additional information or metadata.
     * @param info Information to format
     * @return Formatted information string
     */
    String formatInfo(String info);
}