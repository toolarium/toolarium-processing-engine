/*
 * IProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto;

import java.util.List;

/**
 * The processing unit
 *  
 * @author patrick
 */
public interface IProcessingUnit extends Comparable<IProcessingUnit> {

    /**
     * Get the name
     *
     * @return the name
     */
    String getName();

    
    /**
     * Get the parameter definition list
     *
     * @return the parameter definition list
     */
    List<IParameterDefinition> getParameterDefinitionList();
}
