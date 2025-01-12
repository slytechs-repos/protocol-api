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
 * Default implementation of the TemplateFormatter interface.
 */
public class WiresharkFormatter implements TemplateFormatter {
    private static final String FIELD_FORMAT = "%s: %s";
    private static final String ITEM_FORMAT = "[%s] %s";
    private static final String BRANCH_FORMAT = "%s {%s}";
    
    @Override
    public String formatSummary(Object model) {
        // Format summary based on model type and content
        if (model == null) return "null";
        return String.format("Protocol: %s", model.getClass().getSimpleName());
    }
    
    @Override
    public String formatField(String name, Object value) {
        return String.format(FIELD_FORMAT, name, value);
    }
    
    @Override
    public String formatItem(String name, Object value) {
        return String.format(ITEM_FORMAT, name, value);
    }
    
    @Override
    public String formatBranch(String name, Object value) {
        return String.format(BRANCH_FORMAT, name, value);
    }
    
    @Override
    public String formatInfo(String info) {
        return info;
    }
}