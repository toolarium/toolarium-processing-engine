/*
 * ProcessingEngineListenerImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.listener;

import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.dto.result.IProcessingResult;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
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
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessEnd(com.github.toolarium.processing.engine.dto.result.IProcessingResult)
     */
    @Override
    public void notifyProcessEnd(IProcessingResult processingResult) {
        for (IProcessingListener l : processingListeners) {
            l.notifyProcessEnd(processingResult);
        }
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessingUnitState(java.lang.String, java.lang.String, java.lang.String, com.github.toolarium.processing.unit.dto.ProcessingActionStatus, 
     *      com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void notifyProcessingUnitState(String id, 
                                          String name, 
                                          String processingUnitClass,
                                          ProcessingActionStatus processingActionStatus, 
                                          IProcessingUnitProgress processingUnitProgress,
                                          IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment, 
                                          IProcessingUnitContext processingUnitContext) {
        for (IProcessingListener l : processingListeners) {
            l.notifyProcessingUnitState(id, name, processingUnitClass, processingActionStatus, processingUnitProgress, runtimeTimeMeasurment, processingUnitContext);
        }
    }
}
