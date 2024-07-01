/*
 * IParameterDefinition.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto.parameter;

import com.github.toolarium.processing.unit.dto.ParameterValueType;


/**
 * Defines the parameter definition
 * 
 * @author patrick
 */
public interface IParameterDefinition {
    
    /**
     * Gets the key.
     *
     * @return the key
     */
    String getKey();

    
    /**
     * Gets the data type
     *
     * @return the data type
     */
    ParameterValueType getValueType();

    
    /**
     * Gets the default value
     *
     * @return the default value
     */
    String getDefaultValue();
    
    
    /**
     * Check if the key is optional or not.
     *
     * @return true if it is optional
     */
    boolean isOptional();
    
    
    /**
     * Get the min occurs.
     *
     * @return the min occurs.
     */
    int getMinOccurs();


    /**
     * Get the max occurs.
     *
     * @return the max occurs.
     */
    int getMaxOccurs();
    
    
    /**
     * Check if an empty value is allowed or not.
     *
     * @return true if an empty value is allowed
     */
    boolean isEmptyValueAllowed();

    
    /**
     * Check if the value of this parameter should be protected.
     *
     * @return true if the value of this parameter should be protected; otherwise false.
     */
    boolean hasValueToProtect();

    
    /**
     * Gets the parameter description.
     *
     * @return The parameter description
     */
    String getDescription();
}
