/*
 * ProcessingEngineImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl;

import com.github.toolarium.processing.engine.IProcessEngine;
import com.github.toolarium.processing.engine.IProcessingEngineStatus;
import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.IProcessingUnitRegistry;
import com.github.toolarium.processing.engine.exception.ValidationException;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer;
import com.github.toolarium.processing.engine.impl.executer.impl.ProcessingExecuterImpl;
import com.github.toolarium.processing.engine.impl.listener.ProcessingEngineListenerImpl;
import com.github.toolarium.processing.engine.impl.registry.ProcessingUnitRegistry;
import com.github.toolarium.processing.engine.impl.util.ProcessingPersistenceUtil;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import java.util.Collections;
import java.util.List;


/**
 * Implements the {@link IProcessEngine}.
 * 
 * @author patrick
 */
public class ProcessingEngineImpl implements IProcessEngine {
    private String instanceName;
    private ProcessingExecuterImpl processingExecuter;
    private ProcessingEngineListenerImpl processingEngineListener;
    
    
    /**
     * Constructor for ProcessingEngineImpl
     */
    public ProcessingEngineImpl() {
        instanceName = "";
        processingExecuter = new ProcessingExecuterImpl();
        processingEngineListener = new ProcessingEngineListenerImpl();
        processingExecuter.setProcessingExecuterListener(processingEngineListener);
    }


    /**
     * Constructor for ProcessingEngineImpl
     * 
     * @param persistedContent the persisted content
     */
    public ProcessingEngineImpl(byte[] persistedContent) {
        this();
        
        if (persistedContent != null && persistedContent.length > 0) {
            execute(persistedContent);
        }
    }
    
    
    /**
     * @see com.github.toolarium.processing.engine.IProcessEngine#getInstanceName()
     */
    @Override
    public String getInstanceName() {
        return instanceName;
    }

    
    /**
     * Sets the instance name
     *
     * @param instanceName the instance name
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    
    
    /**
     * Get the processing unit registry
     *
     * @return the processing unit registry
     */
    public IProcessingUnitRegistry getProcessingUnitRegistry() {
        return ProcessingUnitRegistry.getInstance();
        
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessEngine#execute(java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public IProcessingUnitRunnable execute(String id, String name, String processingUnitClass, List<Parameter> parameterList) {
        if (processingUnitClass == null || processingUnitClass.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit!");
        }

        if (!getProcessingUnitRegistry().isRegistered(processingUnitClass)) {
            throw new ValidationException("The referenced procssing unit " + processingUnitClass + " is not registered!");
        }

        // TODO:
        IProcessingUnitContext processingUnitContext = null;
        return processingExecuter.execute(id, 
                                          name, 
                                          getProcessingUnitRegistry().getProcessingUnitList(processingUnitClass).getProcessingClass(), 
                                          parameterList, 
                                          processingUnitContext);
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessEngine#execute(byte[])
     */
    @Override
    public List<IProcessingUnitRunnable> execute(byte[] persistedContent) {
        if (persistedContent != null && persistedContent.length > 0) {
            return processingExecuter.execute(ProcessingPersistenceUtil.getInstance().toProcessingExecuterPersistenceContainer(persistedContent));
        }
        
        return Collections.emptyList();
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessEngine#getStatus()
     */
    @Override
    public IProcessingEngineStatus getStatus() {
        return processingExecuter.getStatus();
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessEngine#addListener(com.github.toolarium.processing.engine.IProcessingListener)
     */
    @Override
    public void addListener(IProcessingListener listener) {
        processingEngineListener.add(listener);
    }
    

    /**
     * @see com.github.toolarium.processing.engine.IProcessEngine#shutdown()
     */
    @Override
    public byte[] shutdown() {
        ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer = processingExecuter.shutdown();
        if (processingExecuterPersistenceContainer == null) {
            return null;
        }
        
        return ProcessingPersistenceUtil.getInstance().toByteArray(processingExecuterPersistenceContainer);
    }
}
