/*
 * ParameterDefinition.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto;

import com.github.toolarium.processing.unit.dto.ParameterValueType;
import java.io.Serializable;
import java.util.Objects;

/**
 * Implements the {@link IParameterDefinition}.
 * 
 * @author patrick
 */
public class ParameterDefinition implements IParameterDefinition, Serializable {
    private static final long serialVersionUID = -5514542112859843338L;
    private final String key;
    private final ParameterValueType valueDataType;
    private final String defaultValue;
    private final boolean isOptional;
    private final int minOccurs;
    private final int maxOccurs;
    private final boolean isEmptyValueAllowed;
    private final boolean hasValueToProtect;
    private final String description;

    
    /**
     * Constructor for ParameterDefinition
     *
     * @param parameterDefinition the parameter definition
     */
    public ParameterDefinition(com.github.toolarium.processing.unit.dto.ParameterDefinition parameterDefinition) {
        this.key = parameterDefinition.getKey();
        this.valueDataType = parameterDefinition.getValueDataType();
        this.defaultValue = "" + parameterDefinition.getDefaultValue();
        this.isOptional = parameterDefinition.isOptional();
        this.minOccurs = parameterDefinition.getMinOccurs();
        this.maxOccurs = parameterDefinition.getMaxOccurs();
        this.isEmptyValueAllowed = parameterDefinition.isEmptyValueAllowed();
        this.hasValueToProtect = parameterDefinition.hasValueToProtect();
        this.description = parameterDefinition.getDescription();
    }
    
    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#getKey()
     */
    @Override
    public String getKey() {
        return key;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#getValueDataType()
     */
    @Override
    public ParameterValueType getValueDataType() {
        return valueDataType;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#getDefaultValue()
     */
    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#isOptional()
     */
    @Override
    public boolean isOptional() {
        return isOptional;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#getMinOccurs()
     */
    @Override
    public int getMinOccurs() {
        return minOccurs;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#getMaxOccurs()
     */
    @Override
    public int getMaxOccurs() {
        return maxOccurs;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#isEmptyValueAllowed()
     */
    @Override
    public boolean isEmptyValueAllowed() {
        return isEmptyValueAllowed;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#hasValueToProtect()
     */
    @Override
    public boolean hasValueToProtect() {
        return hasValueToProtect;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.IParameterDefinition#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, description, hasValueToProtect, isEmptyValueAllowed, isOptional, key,
                maxOccurs, minOccurs, valueDataType);
    }

    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        ParameterDefinition other = (ParameterDefinition) obj;
        return Objects.equals(defaultValue, other.defaultValue) && Objects.equals(description, other.description)
                && hasValueToProtect == other.hasValueToProtect && isEmptyValueAllowed == other.isEmptyValueAllowed
                && isOptional == other.isOptional && Objects.equals(key, other.key) && maxOccurs == other.maxOccurs
                && minOccurs == other.minOccurs && valueDataType == other.valueDataType;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ParameterDefinition [key=" + key + ", valueDataType=" + valueDataType + ", defaultValue=" + defaultValue
                + ", isOptional=" + isOptional + ", minOccurs=" + minOccurs + ", maxOccurs=" + maxOccurs
                + ", isEmptyValueAllowed=" + isEmptyValueAllowed + ", hasValueToProtect=" + hasValueToProtect
                + ", description=" + description + "]";
    }
}
