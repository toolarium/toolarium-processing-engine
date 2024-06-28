/*
 * ProcessingExecuterPersistenceContainer.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Define the processing executer persistence container
 *  
 * @author patrick
 */
public class ProcessingExecuterPersistenceContainer implements Serializable {
    private static final long serialVersionUID = -1201116669971116276L;
    private List<byte[]> suspendedStateList;
    
    
    /**
     * Constructor for ProcessingExecuterSuspendedContent
     */
    public ProcessingExecuterPersistenceContainer() {
        suspendedStateList = new ArrayList<byte[]>();
    }

    
    /**
     * Constructor for ProcessingExecuterSuspendedContent
     * 
     * @param suspendedStateList the suspended state list
     */
    public ProcessingExecuterPersistenceContainer(List<byte[]> suspendedStateList) {
        this.suspendedStateList = suspendedStateList;
    }

    
    /**
     * Add a suspended state
     * @param suspendedState the suspended state
     */
    public void add(byte[] suspendedState) {
        suspendedStateList.add(suspendedState);
    }


    /**
     * Get the list of suspended states
     *
     * @return the suspended state list
     */
    public List<byte[]> getSuspendedStateList() {
        return suspendedStateList;
    }
    
    
    /**
     * Check if it is empty
     *
     * @return true if it is empty
     */
    public boolean isEmpty() {
        return suspendedStateList.isEmpty();
    }
}
