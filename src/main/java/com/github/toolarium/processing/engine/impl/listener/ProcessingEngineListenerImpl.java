/*
 * ProcessingEngineListenerImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.listener;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.dto.result.IProcessingResult;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import java.util.ArrayList;
import java.util.List;


/**
 * Implements the {@link IProcessingListener}.
 *  
 * @author patrick
 */
public class ProcessingEngineListenerImpl implements IProcessingListener {
    private List<IProcessingListener> processingListeners;
    
    /**
     * Constructor for ProcessingEngineImpl.ProcessingEngineListenerImpl
     *
     */
    public ProcessingEngineListenerImpl() {
        processingListeners = new ArrayList<IProcessingListener>();
    }

    
    /**
     * Add a procesing listener
     * 
     * @param listener the listener
     */
    public void add(IProcessingListener listener) {
        processingListeners.add(listener);
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessingUnitProgress(java.lang.String, java.lang.String, java.lang.String, java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext, 
     *      com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.dto.ProcessingActionStatus, com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus, 
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
        for (IProcessingListener processingListener : processingListeners) {
            processingListener.notifyProcessingUnitProgress(id, 
                                                            name, 
                                                            processingUnitClass, 
                                                            parameters,
                                                            processingUnitContext, 
                                                            processingProgress, 
                                                            processingActionStatus, 
                                                            processingRuntimeStatus,
                                                            messages, 
                                                            timeMeasurement, 
                                                            processingUnitThrottling, 
                                                            lastProgressInPercentage);
        }
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
        for (IProcessingListener processingListener : processingListeners) {
            processingListener.notifyProcessingUnitState(id, name, processingUnitClass, previousProcessingActionStatus, processingActionStatus, processingUnitProgress, runtimeTimeMeasurment, processingUnitContext);
        }
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessEnd(com.github.toolarium.processing.engine.dto.result.IProcessingResult)
     */
    @Override
    public void notifyProcessEnd(IProcessingResult processingResult) {
        for (IProcessingListener l : processingListeners) {
            l.notifyProcessEnd(processingResult);
        }
    }
}
