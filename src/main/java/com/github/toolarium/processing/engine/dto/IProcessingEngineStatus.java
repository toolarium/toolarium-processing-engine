/*
 * IProcessingExecuterStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto;


/**
 * Defines the processing engine status
 * 
 * @author patrick
 */
public interface IProcessingEngineStatus {

    /**
     * Get the keep alive time in seconds
     *
     * @return the core pool size
     */
    long getKeepAliveTimeInSeconds();

    
    /**
     * Get the number of running processings
     *
     * @return the number of running processings
     */
    long getNumberOfRunningProcessings();
}
