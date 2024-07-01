/*
 * ProcessingExecutionBuilder.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer;

import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingUnitReference;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.dto.Parameter;
import java.util.ArrayList;
import java.util.List;


/**
 * Defines the p processing execution builder
 * 
 * @author patrick
 */
public class ProcessingExecutionBuilder {
    private List<ProcessingUnitReference> list;
    

    /**
     * Constructor for ProcessingExecutionBuilder
     */
    public ProcessingExecutionBuilder() {
        list = new ArrayList<ProcessingUnitReference>();
        list.add(new ProcessingUnitReference(null, new ArrayList<Parameter>()));
    }
    
    
    /**
     * Set the processing unit execution id
     *
     * @param id the processing unit execution id
     * @return the builder
     */
    public ProcessingExecutionBuilder id(String id) {
        if (list != null && !list.isEmpty()) {
            list.get(list.size() - 1).setId(id);
        }
        return this;
    }

    
    /**
     * Set the processing unit execution name
     *
     * @param name the processing unit execution name
     * @return the builder
     */
    public ProcessingExecutionBuilder name(String name) {
        if (list != null && !list.isEmpty()) {
            list.get(list.size() - 1).setName(name);
        }
        return this;
    }

    
    /**
     * Set the processing unit execution name
     *
     * @param className the processing unit class name
     * @return the builder
     * @throws ClassNotFoundException In case the class can not be resolved
     */
    @SuppressWarnings("unchecked")
    public ProcessingExecutionBuilder processingUnitClass(String className) throws ClassNotFoundException {
        return processingUnitClass((Class<? extends IProcessingUnit>)Class.forName(className));
    }

    
    /**
     * Set the processing unit execution name
     *
     * @param processingUnitClass the processing unit class
     * @return the builder
     * @throws IllegalArgumentException In case of invalid processing unit class
     */
    public ProcessingExecutionBuilder processingUnitClass(Class<? extends IProcessingUnit> processingUnitClass) throws IllegalArgumentException {
        if (list != null && !list.isEmpty()) {
            list.get(list.size() - 1).setProcessingUnitClass(processingUnitClass);
        }

        validateProcessingUnitClass();        
        return this;
    }

    
    /**
     * Set the processing unit execution parameter
     *
     * @param parameter the parameter to add
     * @return the builder
     */
    public ProcessingExecutionBuilder parameter(Parameter parameter) {
        if (list != null && !list.isEmpty()) {
            list.get(list.size() - 1).getParameterList().add(parameter);
        }
        
        return this;
    }

    
    /**
     * Set the processing unit execution parameters
     *
     * @param parameters the parameters to add
     * @return the builder
     */
    public ProcessingExecutionBuilder parameters(Parameter... parameters) {
        for (Parameter p : parameters) {
            if (list != null && !list.isEmpty()) {
                list.get(list.size() - 1).getParameterList().add(p);
            }
        }
        return this;
    }

    
    /**
     * Start a new processing unit execution dependency
     *
     * @return the builder
     * @throws IllegalArgumentException In case of invalid processing unit class
     */
    public ProcessingExecutionBuilder newProcessingUnit() throws IllegalArgumentException {
        validateProcessingUnitClass();
        
        list.add(new ProcessingUnitReference(null, new ArrayList<Parameter>()));
        return this;
    }


    /**
     * Build
     *
     * @return the list of processing unit executions
     * @throws IllegalArgumentException In case of invalid processing unit class
     */
    public List<ProcessingUnitReference> build() throws IllegalArgumentException {
        validateProcessingUnitClass();
        return list;
    }


    /**
     * Validate the last processing unit class
     * 
     * @throws IllegalArgumentException In case of invalid processing unit class
     */
    protected void validateProcessingUnitClass() throws IllegalArgumentException {
        if (list != null && !list.isEmpty() && list.get(list.size() - 1).getProcessingUnitClass() == null) {
            throw new IllegalArgumentException("Can't add a new processing unit because the current don't has any processing unit class!");
        }
    }
}
