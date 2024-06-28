/*
 * IProcessingEngineListener.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine;

import com.github.toolarium.processing.engine.dto.IProcessingResult;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;


/**
 * Defines the processing listener
 * 
 * @author patrick
 */
public interface IProcessingListener {
    
    /**
     * Notify in case of a process end
     *
     * @param processingResult the processing result
     */
    void notifyProcessEnd(IProcessingResult processingResult);

    
    /**
     * Notify processing unit state change
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param processingActionStatus the processing action status
     * @param processingUnitContext the processing unit context
     * @param processingProgress the processing progress 
     */
    void notifyProcessingUnitStateChange(String id, String name, String processingUnitClass, ProcessingActionStatus processingActionStatus, IProcessingUnitContext processingUnitContext, IProcessingProgress processingProgress);
}
