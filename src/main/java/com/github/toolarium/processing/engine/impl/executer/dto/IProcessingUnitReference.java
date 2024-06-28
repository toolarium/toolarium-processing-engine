/*
 * IProcessingUnitReference.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer.dto;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.dto.Parameter;
import java.util.List;


/**
 * Defines the processing unit reference
 *  
 * @author patrick
 */
public interface IProcessingUnitReference {
    
    /**
     * Get the id
     *
     * @return the id
     */
    String getId();
    
    
    /**
     * Get the name
     *
     * @return the name
     */
    String getName();
    
    
    /**
     * Get the processing unit class
     *
     * @return the processing unit class
     */
    Class<? extends IProcessingUnit> getProcessingUnitClass();


    /**
     * Get the parameter list
     *
     * @return the parameter list
     */
    List<Parameter> getParameterList();
}
