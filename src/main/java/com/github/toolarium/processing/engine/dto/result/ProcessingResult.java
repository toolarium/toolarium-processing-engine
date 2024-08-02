/*
 * ProcessingResult.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto.result;

import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;


/**
 * Implements the {@link IProcessingResult}
 *  
 * @author patrick
 */
public class ProcessingResult implements IProcessingResult, Serializable {
    private static final long serialVersionUID = -4202575498425188655L;
    private String id;
    private String name;
    private String processingUnitClassname;
    private Instant startTimestamp;
    private Instant stopTimestamp;
    private long durationInMilliseconds;
    private long numberOfProcessedUnits;
    private long numberOfSuccessfulUnits;
    private long numberOfFailedUnits;
    private int progress;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private boolean isAborted;
    private List<String> statusMessageList;
    private IProcessingStatistic processingStatistic;

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getId()
     */
    @Override
    public String getId() {
        return id;
    }


    /**
     * Set the id
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    
    /**
     * Set the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getProcessingUnitClassname()
     */
    @Override
    public String getProcessingUnitClassname() {
        return processingUnitClassname;
    }

    
    /**
     * Set the processing unit class name
     *
     * @param processingUnitClassname the processing unit class name
     */
    public void setProcessingUnitClassname(String processingUnitClassname) {
        this.processingUnitClassname = processingUnitClassname;
    }


    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getStartTimestamp()
     */
    @Override
    public Instant getStartTimestamp() {
        return startTimestamp;
    }

    
    /**
     * Set the start time stamp
     *
     * @param startTimestamp the start time stamp
     */
    public void setStartTimestamp(Instant startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getStopTimestamp()
     */
    @Override
    public Instant getStopTimestamp() {
        return stopTimestamp;
    }

    
    /**
     * Set the stop time stamp
     *
     * @param stopTimestamp the stop time stamp
     */
    public void setStopTimestamp(Instant stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getProcessingDuration()
     */
    @Override
    public long getProcessingDuration() {
        return durationInMilliseconds;
    }

    
    /**
     * Set the processing duration
     *
     * @param durationInMilliseconds the processing duration
     */
    public void setProcessingDuration(long durationInMilliseconds) {
        this.durationInMilliseconds = durationInMilliseconds;
    }


    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getNumberOfProcessedUnits()
     */
    @Override
    public long getNumberOfProcessedUnits() {
        return numberOfProcessedUnits;
    }

    
    /**
     * Set the number of processed units
     *
     * @param numberOfProcessedUnits the number of processed units
     */
    public void setNumberOfProcessedUnits(long numberOfProcessedUnits) {
        this.numberOfProcessedUnits = numberOfProcessedUnits;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getNumberOfSuccessfulUnits()
     */
    @Override
    public long getNumberOfSuccessfulUnits() {
        return numberOfSuccessfulUnits;
    }

    
    /**
     * Set the number of successful units
     *
     * @param numberOfSuccessfulUnits the number of successful units
     */
    public void setNumberOfSuccessfulUnits(long numberOfSuccessfulUnits) {
        this.numberOfSuccessfulUnits = numberOfSuccessfulUnits;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getNumberOfFailedUnits()
     */
    @Override
    public long getNumberOfFailedUnits() {
        return numberOfFailedUnits;
    }

    
    /**
     * Set the number of failed processed units
     *
     * @param numberOfFailedUnits the number of failed processed units
     */
    public void setNumberOfFailedUnits(long numberOfFailedUnits) {
        this.numberOfFailedUnits = numberOfFailedUnits;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getProgress()
     */
    @Override
    public int getProgress() {
        return progress;
    }

    
    /**
     * Set the progress in percentage
     *
     * @param progress the progress in percentage
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getProcessingRuntimeStatus()
     */
    @Override
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingRuntimeStatus;
    }

    
    /**
     * Set the processing runtime status
     *
     * @param processingRuntimeStatus the processing runtime status
     */
    public void setProcessingRuntimeStatus(ProcessingRuntimeStatus processingRuntimeStatus) {
        this.processingRuntimeStatus = processingRuntimeStatus;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#isAborted()
     */
    @Override
    public boolean isAborted() {
        return isAborted;
    }

    
    /**
     * Set is aborted
     *
     * @param isAborted the true if it is aborted
     */
    public void setIsAborted(boolean isAborted) {
        this.isAborted = isAborted;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getStatusMessageList()
     */
    @Override
    public List<String> getStatusMessageList() {
        return statusMessageList;
    }

    
    /**
     * Set the status message list
     *
     * @param statusMessageList the status message list
     */
    public void setStatusMessageList(List<String> statusMessageList) {
        this.statusMessageList = statusMessageList;
    }


    
    /**
     * @see com.github.toolarium.processing.engine.dto.result.IProcessingResult#getProcesingStatistic()
     */
    @Override
    public IProcessingStatistic getProcesingStatistic() {
        return null;
    }
    
    /**
     * Set the processing statistic
     *
     * @param processingStatistic the processing statistic
     */
    public void setProcessingStatistic(IProcessingStatistic processingStatistic) {
        this.processingStatistic = processingStatistic;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(durationInMilliseconds, id, isAborted, name, numberOfFailedUnits, numberOfProcessedUnits,
                numberOfSuccessfulUnits, processingRuntimeStatus, processingStatistic, processingUnitClassname,
                progress, startTimestamp, statusMessageList, stopTimestamp);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        ProcessingResult other = (ProcessingResult) obj;
        return durationInMilliseconds == other.durationInMilliseconds && Objects.equals(id, other.id)
                && isAborted == other.isAborted && Objects.equals(name, other.name)
                && numberOfFailedUnits == other.numberOfFailedUnits
                && numberOfProcessedUnits == other.numberOfProcessedUnits
                && numberOfSuccessfulUnits == other.numberOfSuccessfulUnits
                && processingRuntimeStatus == other.processingRuntimeStatus
                && Objects.equals(processingStatistic, other.processingStatistic)
                && Objects.equals(processingUnitClassname, other.processingUnitClassname) && progress == other.progress
                && Objects.equals(startTimestamp, other.startTimestamp)
                && Objects.equals(statusMessageList, other.statusMessageList)
                && Objects.equals(stopTimestamp, other.stopTimestamp);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingResult [id=" + id + ", name=" + name + ", processingUnitClassname=" + processingUnitClassname
                + ", startTimestamp=" + startTimestamp + ", stopTimestamp=" + stopTimestamp
                + ", durationInMilliseconds=" + durationInMilliseconds + ", numberOfProcessedUnits="
                + numberOfProcessedUnits + ", numberOfSuccessfulUnits=" + numberOfSuccessfulUnits
                + ", numberOfFailedUnits=" + numberOfFailedUnits + ", progress=" + progress
                + ", processingRuntimeStatus=" + processingRuntimeStatus + ", isAborted=" + isAborted
                + ", statusMessageList=" + statusMessageList + ", processingStatistic=" + processingStatistic + "]";
    }
}
