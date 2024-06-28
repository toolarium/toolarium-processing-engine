/*
 * ProcessingUnitReference.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer.dto;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.dto.Parameter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * Implements the {@link IProcessingUnitReference}.
 * 
 * @author patrick
 */
public class ProcessingUnitReference implements IProcessingUnitReference {
    private String id;
    private String name;
    private Class<? extends IProcessingUnit> processingUnitClass;
    private List<Parameter> parameterList;

    
    /**
     * Constructor for ProcessingUnitReference
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     */
    public ProcessingUnitReference(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList) {
        this(null, null, processingUnitClass, parameterList);
    }

    
    /**
     * Constructor for ProcessingUnitReference
     * 
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     */
    public ProcessingUnitReference(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList) {
        this.id = id;
        this.name = name;
        this.processingUnitClass = processingUnitClass;
        this.parameterList = parameterList;
        
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        
        if (processingUnitClass != null && (this.name == null || this.name.isBlank())) {
            this.name = processingUnitClass.getClass().getName();
        }
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.executer.dto.IProcessingUnitReference#getId()
     */
    @Override
    public String getId() {
        return id;
    }


    /**
     * Set the id
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }



    /**
     * @see com.github.toolarium.processing.engine.impl.executer.dto.IProcessingUnitReference#getName()
     */
    @Override
    public String getName() {
        return name;
    }


    
    /**
     * Set the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.executer.dto.IProcessingUnitReference#getProcessingUnitClass()
     */
    @Override
    public Class<? extends IProcessingUnit> getProcessingUnitClass() {
        return processingUnitClass;
    }


    /**
     * Set the processing unit class
     *
     * @param processingUnitClass the processing unit class
     */
    public void setProcessingUnitClass(Class<? extends IProcessingUnit> processingUnitClass) {
        this.processingUnitClass = processingUnitClass;
    }


    
    /**
     * @see com.github.toolarium.processing.engine.impl.executer.dto.IProcessingUnitReference#getParameterList()
     */
    @Override
    public List<Parameter> getParameterList() {
        return parameterList;
    }


    /**
     * Set the parameter list
     *
     * @param parameterList the parameter list
     */
    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, parameterList, processingUnitClass);
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
        
        ProcessingUnitReference other = (ProcessingUnitReference) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(parameterList, other.parameterList)
                && Objects.equals(processingUnitClass, other.processingUnitClass);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String className = null;
        if (processingUnitClass != null) {
            className = processingUnitClass.getName();
        }
        
        return "ProcessingUnitReference [id=" + id + ", name=" + name + ", processingUnitClass=" + className + ", parameterList=" + parameterList + "]";
    }
}
