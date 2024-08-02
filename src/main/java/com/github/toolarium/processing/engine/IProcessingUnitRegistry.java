/*
 * IProcessingUnitRegistry.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine;

import com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition;
import com.github.toolarium.processing.engine.exception.ValidationException;
import com.github.toolarium.processing.unit.IProcessingUnit;
import java.util.Set;


/**
 * Defines the processing unit registry of the processing engine.
 * 
 * @author patrick
 */
public interface IProcessingUnitRegistry {
    
    /**
     * Register a processing unit
     *
     * @param processingUnit the processing unit class
     * @return the registered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnitDefinition register(Class<? extends IProcessingUnit> processingUnit) throws ValidationException;


    /**
     * Register a processing unit
     *
     * @param processingUnitClassName the processing unit class name
     * @return the registered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnitDefinition register(String processingUnitClassName) throws ValidationException;


    /**
     * Unregister a processing unit
     *
     * @param processingUnit the processing unit class
     * @return the unregistered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnitDefinition unregister(Class<? extends IProcessingUnit> processingUnit) throws ValidationException;


    /**
     * Unregister a processing unit
     *
     * @param processingUnitClassName the processing unit class name
     * @return the unregistered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnitDefinition unregister(String processingUnitClassName) throws ValidationException;

    
    /**
     * Check if the processing unit is registered
     *
     * @param processingUnit the processing unit class
     * @return true if it is already registered
     */
    boolean isRegistered(Class<? extends IProcessingUnit> processingUnit);


    /**
     * Check if the processing unit is registered
     *
     * @param processingUnitClassName the processing unit class name
     * @return true if it is already registered
     */
    boolean isRegistered(String processingUnitClassName);

    
    /**
     * Get the processing units which are registered
     *
     * @param processingUnitClassName the processing unit class name
     * @return the processing unit reference
     */
    IProcessingUnitDefinition getProcessingUnitList(String processingUnitClassName);


    /**
     * Search the available processing units which are registered
     *
     * @param filterName the filter name
     * @return processing unit the reference list
     */
    Set<IProcessingUnitDefinition> searchProcessingUnitList(String filterName);
}
