/*
 * ProcessingExecuterStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.status;

/**
 * Implements the {@link IProcessingExecuterStatus}.
 * 
 * @author patrick
 */
public class ProcessingExecuterStatus implements IProcessingExecuterStatus {
    private int corePoolSize = 0;
    private int maximumPoolSize = Integer.MAX_VALUE;
    private long keepAliveTimeInSeconds = 60L;
    private long numberOfRunningProcessings;


    /**
     * @see com.github.toolarium.processing.engine.impl.status.IProcessingExecuterStatus#getCorePoolSize()
     */
    @Override
    public int getCorePoolSize() {
        return corePoolSize;
    }

    
    /**
     * Set the core pool size
     *
     * @param corePoolSize the core pool size
     */
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.status.IProcessingExecuterStatus#getMaxPoolSize()
     */
    @Override
    public int getMaxPoolSize() {
        return maximumPoolSize;
    }

    
    /**
     * Set the max pool size
     *
     * @param maximumPoolSize the max pool size
     */
    public void setMaxPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.status.IProcessingExecuterStatus#getKeepAliveTimeInSeconds()
     */
    @Override
    public long getKeepAliveTimeInSeconds() {
        return keepAliveTimeInSeconds;
    }

    
    /**
     * Set the keep alive time in seconds
     *
     * @param keepAliveTimeInSeconds the keep alive time in seconds
     */
    public void setKeepAliveTimeInSeconds(long keepAliveTimeInSeconds) {
        this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.impl.status.IProcessingExecuterStatus#getNumberOfRunningProcessings()
     */
    @Override
    public long getNumberOfRunningProcessings() {
        return numberOfRunningProcessings;
    }
    
    
    /**
     * Set the number of running processings
     *
     * @param numberOfRunningProcessings numberOfRunningProcessings
     */
    public void setNumberOfRunningProcessings(long numberOfRunningProcessings) {
        this.numberOfRunningProcessings = numberOfRunningProcessings;
    }
     
}
