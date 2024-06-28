/*
 * ProcessingFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine;

import com.github.toolarium.processing.engine.impl.ProcessingEngineImpl;


/**
 * Defines the processing engine factory
 *  
 * @author patrick
 */
public final class ProcessingEngineFactory {
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ProcessingEngineFactory INSTANCE = new ProcessingEngineFactory();
    }

    
    /**
     * Constructor
     */
    private ProcessingEngineFactory() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ProcessingEngineFactory getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * Get a processing executer
     * 
     * @return a processing executer
     */
    public IProcessEngine getProcessingEngine() {
        IProcessEngine processEngine = new ProcessingEngineImpl(); 
        return processEngine;
    }

    
    /**
     * Get a processing executer
     * 
     * @param persistedContent the persisted content from a shutdown
     * @return a processing executer
     */
    public IProcessEngine getProcessingEngine(byte[] persistedContent) {
        IProcessEngine processEngine = new ProcessingEngineImpl(persistedContent); 
        return processEngine;
    }
}
