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
package com.slytechs.jnet.protocol.api.meta.template;

/**
 * Interface for output printing operations.
 */
public interface ProtocolPrinter {
    /**
     * Print a summary line.
     * @param summary The summary text to print
     * @throws Exception If printing fails
     */
    void printSummary(String summary) throws Exception;
    
    /**
     * Print a field with specified indentation.
     * @param field The field text to print
     * @param indent Indentation level
     * @param isOpen Whether this is part of an open branch
     * @throws Exception If printing fails
     */
    void printField(String field, int indent, boolean isOpen) throws Exception;
    
    /**
     * Print an item with specified indentation.
     * @param item The item text to print
     * @param indent Indentation level
     * @throws Exception If printing fails
     */
    void printItem(String item, int indent) throws Exception;
    
    /**
     * Print a branch with specified indentation.
     * @param branch The branch text to print
     * @param indent Indentation level
     * @param isOpen Whether this branch is open
     * @throws Exception If printing fails
     */
    void printBranch(String branch, int indent, boolean isOpen) throws Exception;
    
    /**
     * Print additional information.
     * @param info The information text to print
     * @throws Exception If printing fails
     */
    void printInfo(String info) throws Exception;
}