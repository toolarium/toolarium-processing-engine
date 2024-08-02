/*
 * IProcessingUnitDefinition.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto.unit;

import com.github.toolarium.processing.engine.dto.parameter.IParameterDefinition;
import com.github.toolarium.processing.engine.exception.ValidationException;
import com.github.toolarium.processing.unit.IProcessingUnit;
import java.util.List;


/**
 * The processing unit definition
 *  
 * @author patrick
 */
public interface IProcessingUnitDefinition extends Comparable<IProcessingUnitDefinition> {

    /**
     * Get the processing unit class name
     *
     * @return the processing unit class name
     */
    String getProcessingClassname();
    
    
    /**
     * Resolve processing unit class
     *  
     * @return the processing unit class
     * @throws ValidationException In case of invalid parameters
     */
    Class<? extends IProcessingUnit> getProcessingClass();

    
    /**
     * Get the parameter definition list
     *
     * @return the parameter definition list
     */
    List<IParameterDefinition> getParameterDefinitionList();
}
