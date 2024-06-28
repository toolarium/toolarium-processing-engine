/*
 * IProcessingExecuter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer;

import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingUnitReference;
import com.github.toolarium.processing.engine.impl.status.IProcessingExecuterStatus;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import java.util.List;


/**
 * Defines the process executer
 * 
 * @author patrick
 */
public interface IProcessingExecuter {

    /**
     * Execute a processing with its parameter
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @return the added {@link IProcessingUnitRunnable}.
     */
    IProcessingUnitRunnable execute(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList);

    
    /**
     * Execute a processing with its parameter
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @return the added {@link IProcessingUnitRunnable}.
     */
    IProcessingUnitRunnable execute(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList);

    
    /**
     * Execute a processing with its parameter
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing context.
     * @return the added {@link IProcessingUnitRunnable}.
     */
    IProcessingUnitRunnable execute(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext);

    
    /**
     * Execute a processing with its parameter
     *
     * @param processingUnitReferenceList the processing unit reference list 
     * @param processingUnitContext the processing context.
     * @return the added {@link IProcessingUnitRunnable}.
     */
    List<IProcessingUnitRunnable> execute(List<ProcessingUnitReference> processingUnitReferenceList, IProcessingUnitContext processingUnitContext);

    
    /**
     * After shutdown this execute method resumes suspended processing units
     *
     * @param processingExecuterPersistenceContainer the processing executer persistence container 
     * @return the added {@link IProcessingUnitRunnable}s.
     */
    List<IProcessingUnitRunnable> execute(ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer);

    
    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     * 
     * @return the processing executer persistence container
     */
    ProcessingExecuterPersistenceContainer shutdown();
    
    
    /**
     * Get the status back
     *
     * @return the status
     */
    IProcessingExecuterStatus getStatus();    
}
