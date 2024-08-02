/*
 * ProcessingUnitDefinition.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto.unit;

import com.github.toolarium.processing.engine.dto.parameter.IParameterDefinition;
import com.github.toolarium.processing.unit.IProcessingUnit;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * Implements the {@link IProcessingUnitDefinition}.
 *  
 * @author patrick
 */
public class ProcessingUnitDefinition implements IProcessingUnitDefinition, Serializable, Comparable<IProcessingUnitDefinition> {
    private static final long serialVersionUID = 1960750581527024229L;
    private String processingClassname;
    private Class<? extends IProcessingUnit> processingClass;
    private List<IParameterDefinition> parameterDefinitionList;

    
    /**
     * Constructor for ProcessingUnit
     */
    public ProcessingUnitDefinition() {
    }

    
    /**
     * Constructor for ProcessingUnitDefinition
     * 
     * @param processingClass the processing class
     * @param parameterDefinitionList the parameter definition list
     */
    public ProcessingUnitDefinition(Class<? extends IProcessingUnit> processingClass, List<IParameterDefinition> parameterDefinitionList) {
        setProcessingClass(processingClass);
        this.parameterDefinitionList = parameterDefinitionList;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition#getProcessingClassname()
     */
    @Override
    public String getProcessingClassname() {
        return processingClassname;
    }
    
    
    /**
     * @see com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition#getProcessingClass()
     */
    @Override
    public Class<? extends IProcessingUnit> getProcessingClass() {
        return processingClass;
    }

    
    /**
     * Sets the processing class
     *
     * @param processingClass the processing class
     */
    public void setProcessingClass(Class<? extends IProcessingUnit> processingClass) {
        this.processingClass = processingClass;
        this.processingClassname = processingClass.getName();
    }


    /**
     * @see com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition#getParameterDefinitionList()
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
    public int compareTo(IProcessingUnitDefinition o) {
        return getProcessingClassname().compareTo(o.getProcessingClassname());
    }
    

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(processingClassname, parameterDefinitionList);
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
        
        ProcessingUnitDefinition other = (ProcessingUnitDefinition) obj;
        return Objects.equals(processingClassname, other.processingClassname)
                && Objects.equals(parameterDefinitionList, other.parameterDefinitionList);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnit [name=" + processingClassname + ", parameterDefinitionList=" + parameterDefinitionList + "]";
    }
}
