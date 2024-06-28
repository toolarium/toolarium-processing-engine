/*
 * IProcessingExecuterResult.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto;

import com.github.toolarium.processing.unit.IProcessingStatistic;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import java.time.Instant;
import java.util.List;


/**
 * Defines the processing result.
 *  
 * @author patrick
 */
public interface IProcessingResult {
    
    /**
     * Get the instance
     *
     * @return the instance
     */
    String getInstance();
    
    
    /**
     * Get the id
     *
     * @return the id
     */
    String getId();
    
    
    /**
     * Get the name
     *
     * @return the name
     */
    String getName();

    
    /**
     * Get the start time stamp
     *
     * @return the start time stamp
     */
    Instant getStartTimestamp();
    
    
    /**
     * Get the end time stamp when it has ended
     *
     * @return the end time stamp
     */
    Instant getStopTimestamp();
    
    
    /**
     * Get the processing duration in milliseconds (excluded possible suspending)
     *
     * @return the processing duration in milliseconds
     */
    long getProcessingDuration();

    
    /**
     * True if the processing is aborted
     *
     * @return true if it is aborted
     */
    boolean isAborted();
    

    /**
     * Gets the number of processed units (failed units included).
     *
     * @return the number of processed units (failed units included).
     */
    long getNumberOfProcessedUnits();

    
    /**
     * Gets the number of successful units.
     *
     * @return the number of successful units.
     */
    long getNumberOfSuccessfulUnits();

    
    /**
     * Gets the number of failed units.
     *
     * @return the number of failed units.
     */
    long getNumberOfFailedUnits();


    /**
     * The processing runtime status which covers the overall status
     *
     * @return the processing status type.
     */
    ProcessingRuntimeStatus getProcessingRuntimeStatus();
    

    /**
     * Returns the processing status message
     *
     * @return the processing status message
     */
    List<String> getStatusMessageList();


    /**
     * The processing statistic.
     *
     * @return the processing statistic.
     */
    IProcessingStatistic getProcesingStatistic();
}
