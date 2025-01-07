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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Evaluates compiled expressions with comprehensive debugging support.
 * This class adds debug hooks and event tracking while maintaining performance
 * when debugging is disabled.
 */
public final class ExpressionEvaluator {
    
    /** The root node of the expression tree to evaluate */
    private final ExprNode root;
    
    /** The function used to resolve variable references */
    private final Function<String, Number> varResolver;
    
    /** Cache of variable values for the current evaluation */
    private final Map<String, Integer> localVariableCache;
    
    /** Tracks if debug mode is enabled */
    private boolean debugEnabled;
    
    /** Collects debug events during evaluation */
    private final List<DebugEvent> debugEvents;
    
    /** Tracks evaluation depth for debug indentation */
    private int evaluationDepth;
    
    /**
     * Types of debug events that can occur during evaluation.
     */
    public enum DebugEventType {
        NODE_ENTER,         // Starting evaluation of a node
        NODE_EXIT,          // Finished evaluation of a node
        VARIABLE_RESOLVE,   // Variable lookup
        VARIABLE_CACHE_HIT, // Variable found in cache
        CACHE_UPDATE,       // Cache value modified
        ERROR              // Error during evaluation
    }
    
    /**
     * Represents a single debug event during evaluation.
     */
    public static final class DebugEvent {
        private final DebugEventType type;
        private final String description;
        private final int depth;
        private final long timestamp;
        
        DebugEvent(DebugEventType type, String description, int depth) {
            this.type = type;
            this.description = description;
            this.depth = depth;
            this.timestamp = System.nanoTime();
        }
        
        public DebugEventType getType() { return type; }
        public String getDescription() { return description; }
        public int getDepth() { return depth; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Creates a new evaluator with debug support.
     */
    ExpressionEvaluator(ExprNode root, Function<String, Number> varResolver) {
        if (root == null || varResolver == null) {
            throw new NullPointerException("Root node and variable resolver cannot be null");
        }
        this.root = root;
        this.varResolver = varResolver;
        this.localVariableCache = new HashMap<>();
        this.debugEvents = new ArrayList<>();
        this.evaluationDepth = 0;
    }
    
    /**
     * Sets debug mode status.
     */
    public void setDebugEnabled(boolean enabled) {
        this.debugEnabled = enabled;
        if (!enabled) {
            debugEvents.clear();
        }
    }
    
    /**
     * Checks if debug mode is enabled.
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }
    
    /**
     * Gets a copy of the debug events list.
     */
    public List<DebugEvent> getDebugEvents() {
        return new ArrayList<>(debugEvents);
    }
    
    /**
     * Records a debug event if debug mode is enabled.
     */
    private void recordDebugEvent(DebugEventType type, String description) {
        if (debugEnabled) {
            debugEvents.add(new DebugEvent(type, description, evaluationDepth));
        }
    }
    
    /**
     * Evaluates the expression with debug support if enabled.
     */
    public int run() {
        return run(new Object[0]);
    }
    
    /**
     * Internal method to resolve a variable, with caching.
     */
    private Number resolveVariableInternal(String name) {
        // Check cache first
        if (localVariableCache.containsKey(name)) {
            int cachedValue = localVariableCache.get(name);
            recordDebugEvent(DebugEventType.VARIABLE_CACHE_HIT, 
                String.format("Cache hit for %s = %d", name, cachedValue));
            return cachedValue;
        }
        
        // Resolve using provided resolver
        recordDebugEvent(DebugEventType.VARIABLE_RESOLVE, "Resolving " + name);
        Number value = varResolver.apply(name);
        if (value == null) {
            String msg = "Undefined variable: " + name;
            recordDebugEvent(DebugEventType.ERROR, msg);
            throw new ExpressionException(msg);
        }
        
        // Cache the value
        int intValue = value.intValue();
        localVariableCache.put(name, intValue);
        recordDebugEvent(DebugEventType.CACHE_UPDATE, 
            String.format("Cached %s = %d", name, intValue));
        
        return intValue;
    }
    
    /** ThreadLocal storage for current evaluation arguments */
    private static final ThreadLocal<Object[]> currentArguments = new ThreadLocal<>();
    
    /**
     * Gets the current evaluation arguments.
     * Package private, used by ArgumentNode.
     */
    static Object[] getCurrentArguments() {
        return currentArguments.get();
    }
    
    /**
     * Evaluates the expression with context and debug support.
     */
    public int run(Object... args) {
        try {
            currentArguments.set(args);
            localVariableCache.clear();
            debugEvents.clear();
            evaluationDepth = 0;
            
            recordDebugEvent(DebugEventType.NODE_ENTER, "Starting evaluation");
            int result = root.evaluate(this::resolveVariableInternal);
            recordDebugEvent(DebugEventType.NODE_EXIT, "Result: " + result);
            return result;
            
        } catch (ExpressionException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Evaluation failed: " + e.getMessage();
            recordDebugEvent(DebugEventType.ERROR, msg);
            throw new ExpressionException(msg, e);
        } finally {
            currentArguments.remove();
        }
    }
    
    /**
     * Gets the cached value for a variable.
     */
    public Integer getCachedValue(String name) {
        return localVariableCache.get(name);
    }
    
    /**
     * Checks if a variable is cached.
     */
    public boolean isCached(String name) {
        return localVariableCache.containsKey(name);
    }
    
    /**
     * Clears the variable cache.
     */
    public void clearCache() {
        localVariableCache.clear();
        recordDebugEvent(DebugEventType.CACHE_UPDATE, "Cache cleared");
    }
}