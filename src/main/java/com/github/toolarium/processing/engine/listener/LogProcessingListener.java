/*
 * LogProcessingListener.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.listener;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.common.formatter.TimeDifferenceFormatter;
import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.dto.result.IProcessingResult;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatistic;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a {@link IProcessingListener} which logs the satus.
 *  
 * @author patrick
 */
public class LogProcessingListener implements IProcessingListener {    
    private static ThreadLocal<TimeDifferenceFormatter> timeDifferenceFormatter = ThreadLocal.withInitial(() -> new TimeDifferenceFormatter());    
    private static final Logger LOG = LoggerFactory.getLogger(LogProcessingListener.class);
    private Logger logger;
    private String prefix;

    
    /**
     * Constructor for LogProcessingListener
     */
    public LogProcessingListener() {
        this(LOG);
    }

    
    /**
     * Constructor for LogProcessingListener
     * 
     * @param logger the logger to use
     */
    public LogProcessingListener(final Logger logger) {
        this.logger = logger;
        this.prefix = "";
        
        if (this.logger == null) {
            this.logger = LOG;
        }
    }
    
    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessingUnitProgress(java.lang.String, java.lang.String, java.lang.String, java.util.List, 
     *      com.github.toolarium.processing.unit.IProcessingUnitContext, com.github.toolarium.processing.unit.IProcessingUnitProgress, 
     *      com.github.toolarium.processing.unit.dto.ProcessingActionStatus, com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus, 
     *      java.util.List, com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement, com.github.toolarium.common.bandwidth.IBandwidthThrottling, int)
     */
    @Override
    public void notifyProcessingUnitProgress(String id, // CHECKSTYLE IGNORE THIS LINE
                                             String name, 
                                             String processingUnitClass,
                                             List<Parameter> parameters, 
                                             IProcessingUnitContext processingUnitContext,
                                             IProcessingUnitProgress processingProgress, 
                                             ProcessingActionStatus processingActionStatus,
                                             ProcessingRuntimeStatus processingRuntimeStatus, 
                                             List<String> messages,
                                             IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                                             IBandwidthThrottling processingUnitThrottling,
                                             int lastProgressInPercentage) {
        String progressMessage = ProcessingUnitUtil.getInstance().toString(id, 
                                                                           name, 
                                                                           processingUnitClass, 
                                                                           parameters, 
                                                                           processingUnitContext, 
                                                                           processingProgress, 
                                                                           processingActionStatus, 
                                                                           processingRuntimeStatus, 
                                                                           messages, 
                                                                           timeMeasurement, 
                                                                           processingUnitThrottling);
        //lastProgressInPercentage        
        logger.debug(prefix + progressMessage);
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessingUnitState(java.lang.String, java.lang.String, java.lang.String, com.github.toolarium.processing.unit.dto.ProcessingActionStatus, 
     *      com.github.toolarium.processing.unit.dto.ProcessingActionStatus, com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement, 
     *      com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void notifyProcessingUnitState(String id, 
                                          String name, 
                                          String processingUnitClass, 
                                          ProcessingActionStatus previousProcessingActionStatus,
                                          ProcessingActionStatus processingActionStatus,
                                          IProcessingUnitProgress processingUnitProgress,
                                          IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment,
                                          IProcessingUnitContext processingUnitContext) {

        //final String state = ProcessingUnitUtil.getInstance().toString(id, 
        //                                                               name, 
        //                                                               processingUnitClass, 
        //                                                               null, /* parameters */
        //                                                               processingUnitContext,
        //                                                               processingUnitProgress, 
        //                                                               processingActionStatus, 
        //                                                               null, /* processingRuntimeStatus */
        //                                                               null, /* messages */
        //                                                               runtimeTimeMeasurment, 
        //                                                               null /* processingUnitThrottling */);
        //logger.info(prefix + state);

        if (LOG.isInfoEnabled()) {
            String message = "";
            if (previousProcessingActionStatus != null) {
                message = "from " + previousProcessingActionStatus + " -> " + processingActionStatus;
            } else {
                message = "to " + processingActionStatus;
            }
            
            LOG.info(prefix + ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass) + " Status changed " + message);
        }
    }        
    
    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessEnd(com.github.toolarium.processing.engine.dto.result.IProcessingResult)
     */
    @Override
    public void notifyProcessEnd(IProcessingResult processingResult) {
        StringBuilder builder = new StringBuilder();
        builder.append(ProcessingUnitUtil.getInstance().toString(processingResult.getId(),
                                                                 processingResult.getName(), 
                                                                 processingResult.getProcessingUnitClassname(),
                                                                 null, // List<Parameter> parameters
                                                                 null, // IProcessingUnitContext processingUnitContext
                                                                 createProcessingUnitProgress(processingResult), /*,*/ 
                                                                 null, // ProcessingActionStatus processingActionStatus
                                                                 processingResult.getProcessingRuntimeStatus(), 
                                                                 processingResult.getStatusMessageList(),
                                                                 createTimeMeasurement(processingResult), 
                                                                 null)); // IBandwidthThrottling processingUnitThrottling                                                    
        
        
        logger.info(builder.toString());
    }

    
    /**
     * Create a {@link IProcessingUnitProgress}. 
     * 
     * @param processingResult the processing result
     * @return the processing unit progress
     */
    protected IProcessingUnitProgress createProcessingUnitProgress(final IProcessingResult processingResult) {
        return new IProcessingUnitProgress() {

            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProcessingRuntimeStatus()
             */
            @Override
            public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
                return null;
            }


            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProcessingUnitStatistic()
             */
            @Override
            public IProcessingUnitStatistic getProcessingUnitStatistic() {
                return null;
            }

            
            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfUnprocessedUnits()
             */
            @Override
            public long getNumberOfUnprocessedUnits() {
                return 0;
            }

            
            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfUnitsToProcess()
             */
            @Override
            public long getNumberOfUnitsToProcess() {
                return 0;
            }

            
            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfSuccessfulUnits()
             */
            @Override
            public long getNumberOfSuccessfulUnits() {
                return processingResult.getNumberOfSuccessfulUnits();
            }

            
            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfProcessedUnits()
             */
            @Override
            public long getNumberOfProcessedUnits() {
                return processingResult.getNumberOfProcessedUnits();
            }

            
            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfFailedUnits()
             */
            @Override
            public long getNumberOfFailedUnits() {
                return processingResult.getNumberOfFailedUnits();
            }


            /**
             * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProgress()
             */
            @Override
            public int getProgress() {
                return processingResult.getProgress();
            }
        };
    }


    /**
     * Create the {@link IProcessingUnitRuntimeTimeMeasurement}.
     * 
     * @param processingResult the processing result
     * @return the processing unit time measurement 
     */
    protected IProcessingUnitRuntimeTimeMeasurement createTimeMeasurement(final IProcessingResult processingResult) {
        return new IProcessingUnitRuntimeTimeMeasurement() {

            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getStartTimestamp()
             */
            @Override
            public Instant getStartTimestamp() {
                return processingResult.getStartTimestamp();
            }

            
            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getStopTimestamp()
             */
            @Override
            public Instant getStopTimestamp() {
                return processingResult.getStopTimestamp();
            }

            
            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getDurationAsString()
             */
            @Override
            public String getDurationAsString() {
                return timeDifferenceFormatter.get().formatAsString(processingResult.getProcessingDuration());
            }

            
            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getDuration()
             */
            @Override
            public long getDuration() {
                return processingResult.getProcessingDuration();
            }
        };
    }
}
