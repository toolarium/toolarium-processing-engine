/*
 * IProcessEngine.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine;

import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import java.util.List;


/**
 * Defines the process engine
 *  
 * @author patrick
 */
public interface IProcessEngine {
    
    /**
     * Get the process engine instance name
     *
     * @return the process engine instance name
     */
    String getInstanceName();
    
    
    /**
     * Get the processing unit registry
     *
     * @return the processing unit registry
     */
    IProcessingUnitRegistry getProcessingUnitRegistry();
    
    
    /**
     * Execute the process unit
     *
     * @param id the id of the execution or null
     * @param name the name of the execution
     * @param processingUnitClass the process unit class
     * @param parameterList the parameter list
     * @return the added {@link IProcessingUnitRunnable}.
     */
    IProcessingUnitRunnable execute(String id, String name, String processingUnitClass, List<Parameter> parameterList);

    
    /**
     * Get the status back
     *
     * @return the status
     */
    IProcessingEngineStatus getStatus();    

    
    /**
     * Add a {@link IProcessingListener}.
     *
     * @param listener the listener to add
     */
    void addListener(IProcessingListener listener);
    
    
    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     * 
     * @return the processing suspended state
     */
    byte[] shutdown();
}
