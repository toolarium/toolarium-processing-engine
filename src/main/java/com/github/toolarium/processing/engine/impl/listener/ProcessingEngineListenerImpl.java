/*
 * ProcessingEngineListenerImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.listener;

import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.dto.IProcessingResult;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
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
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessEnd(com.github.toolarium.processing.engine.dto.IProcessingResult)
     */
    @Override
    public void notifyProcessEnd(IProcessingResult processingResult) {
        for (IProcessingListener l : processingListeners) {
            l.notifyProcessEnd(processingResult);
        }
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessingUnitStateChange(java.lang.String, java.lang.String, java.lang.String, com.github.toolarium.processing.unit.dto.ProcessingActionStatus, 
     *      com.github.toolarium.processing.unit.IProcessingUnitContext, com.github.toolarium.processing.unit.IProcessingProgress)
     */
    @Override
    public void notifyProcessingUnitStateChange(String id, String name, String processingUnitClass, ProcessingActionStatus processingActionStatus, IProcessingUnitContext processingUnitContext, IProcessingProgress processingProgress) {
        for (IProcessingListener l : processingListeners) {
            l.notifyProcessingUnitStateChange(id, name, processingUnitClass, processingActionStatus, processingUnitContext, processingProgress);
        }
    }
}
