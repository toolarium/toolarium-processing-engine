/*
 * ProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto.unit;

import com.github.toolarium.processing.engine.dto.parameter.IParameterDefinition;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * Implements the {@link IProcessingUnit}.
 *  
 * @author patrick
 */
public class ProcessingUnit implements IProcessingUnit, Serializable, Comparable<IProcessingUnit> {
    private static final long serialVersionUID = 1960750581527024229L;
    private String name;
    private List<IParameterDefinition> parameterDefinitionList;

    
    /**
     * Constructor for ProcessingUnit
     */
    public ProcessingUnit() {
    }

    
    /**
     * Constructor for ProcessingUnit
     * 
     * @param name the name
     * @param parameterDefinitionList the parameter definition list
     */
    public ProcessingUnit(String name, List<IParameterDefinition> parameterDefinitionList) {
        this.name = name;
        this.parameterDefinitionList = parameterDefinitionList;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.unit.IProcessingUnit#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    
    
    /**
     * Sets the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @see com.github.toolarium.processing.engine.dto.unit.IProcessingUnit#getParameterDefinitionList()
     */
    @Override
    public List<IParameterDefinition> getParameterDefinitionList() {
        return parameterDefinitionList;
    }

    
    /**
     * Set the parameter definition list
     *
     * @param parameterDefinitionList the parameter definition list
     */
    public void setParameterDefinitionList(List<IParameterDefinition> parameterDefinitionList) {
        this.parameterDefinitionList = parameterDefinitionList;
    }

    
    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(IProcessingUnit o) {
        return getName().compareTo(o.getName());
    }
    

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, parameterDefinitionList);
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
        
        ProcessingUnit other = (ProcessingUnit) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(parameterDefinitionList, other.parameterDefinitionList);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnit [name=" + name + ", parameterDefinitionList=" + parameterDefinitionList + "]";
    }
}
