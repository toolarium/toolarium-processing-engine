/*
 * IProcessingUnitRegistry.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine;

import com.github.toolarium.processing.engine.dto.unit.IProcessingUnit;
import com.github.toolarium.processing.engine.exception.ValidationException;
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
    IProcessingUnit register(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnit) throws ValidationException;


    /**
     * Register a processing unit
     *
     * @param processingUnitClassName the processing unit class name
     * @return the registered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnit register(String processingUnitClassName) throws ValidationException;


    /**
     * Unregister a processing unit
     *
     * @param processingUnit the processing unit class
     * @return the unregistered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnit unregister(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnit) throws ValidationException;


    /**
     * Unregister a processing unit
     *
     * @param processingUnitClassName the processing unit class name
     * @return the unregistered processing unit
     * @throws ValidationException In case of a validation exception
     */
    IProcessingUnit unregister(String processingUnitClassName) throws ValidationException;

    
    /**
     * Check if the processing unit is registered
     *
     * @param processingUnit the processing unit class
     * @return true if it is already registered
     */
    boolean isRegistered(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnit);


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
    IProcessingUnitReference getProcessingUnitList(String processingUnitClassName);


    /**
     * Search the available processing units which are registered
     *
     * @param filterName the filter name
     * @return processing unit the reference list
     */
    Set<IProcessingUnitReference> searchProcessingUnitList(String filterName);

    
    /**
     * Get the processing unit instance
     *
     * @param processingUnitClassName the processing unit class name
     * @return the processing unit instance
     * @throws ValidationException In case of a validation exception
     */
    com.github.toolarium.processing.unit.IProcessingUnit newInstance(String processingUnitClassName) throws ValidationException;



    /**
     * Defines the processing unit holder
     * 
     * @author patrick
     */
    interface IProcessingUnitReference {

        /**
         * Get the processing unit
         *
         * @return the processing unit
         */
        IProcessingUnit getProcessingUnit();

        /**
         * Get the processing unit class
         *
         * @return the processing unit class
         */
        Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> getProcessingUnitClass(); // TODO: string
    }
}
