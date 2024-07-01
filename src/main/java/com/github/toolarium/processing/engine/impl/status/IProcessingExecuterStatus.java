/*
 * IProcessingExecuterStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.status;

import com.github.toolarium.processing.engine.IProcessingEngineStatus;

/**
 * Defines the processing executer status
 * 
 * @author patrick
 */
public interface IProcessingExecuterStatus extends IProcessingEngineStatus {

    /**
     * Get the core pool size
     *
     * @return the core pool size
     */
    int getCorePoolSize();

    
    /**
     * Get the max pool size
     *
     * @return the core pool size
     */
    int getMaxPoolSize();
}
