/*
 * ProcessingPersistence.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.util;

import com.github.toolarium.common.util.TextUtil;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Defines the procesing persistence utility.
 *  
 * @author patrick
 */
public final class ProcessingPersistenceUtil {

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ProcessingPersistenceUtil INSTANCE = new ProcessingPersistenceUtil();
    }

    
    /**
     * Constructor
     */
    private ProcessingPersistenceUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ProcessingPersistenceUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Convert the object into a byte array
     *
     * @param processingExecuterPersistenceContainer the processing executer persistence container
     * @return the byte array to persist
     * @throws ProcessingException In case the processing container can't be serialized properly 
     */
    public byte[] toByteArray(ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer) throws ProcessingException {
        if (processingExecuterPersistenceContainer == null || processingExecuterPersistenceContainer.isEmpty()) {
            return null;
        }
        
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(outputStream);
            objOutStream.writeObject(processingExecuterPersistenceContainer);
            objOutStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (RuntimeException | IOException e) {
            throw new ProcessingException("Could not serialize processing persistence conatiner [" + processingExecuterPersistenceContainer.getClass() + "]: " + e.getMessage(), e, true);
        }
    }

    
    /**
     * Convert the object into a byte array
     *
     * @param persistedState the persisted state
     * @return the object representation
     * @throws ProcessingException In case the processing container can't be de-serialized properly 
     */
    public ProcessingExecuterPersistenceContainer toProcessingExecuterPersistenceContainer(byte[] persistedState) throws ProcessingException {
        if (persistedState == null || persistedState.length <= 0) {
            return null;
        }
        
        try {
            ObjectInputStream objInStream = new ObjectInputStream(new ByteArrayInputStream(persistedState));
            ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer = (ProcessingExecuterPersistenceContainer)objInStream.readObject();
            objInStream.close();
            return processingExecuterPersistenceContainer;
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            throw new ProcessingException("Could not de-serialize processing persistence conatiner: " + e.getMessage(), e, true);
        }
    }
    
    
    /**
     * Get the persisted state as string 
     *
     * @param persistedState the persisted state
     * @return the string re-presenation
     */
    public String toString(byte[] persistedState) {
        ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer = toProcessingExecuterPersistenceContainer(persistedState);
        if (processingExecuterPersistenceContainer == null || processingExecuterPersistenceContainer.getSuspendedStateList().isEmpty()) {
            return null;
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < processingExecuterPersistenceContainer.getSuspendedStateList().size(); i++) {
            builder.append(ProcessingUnitUtil.getInstance().toString(processingExecuterPersistenceContainer.getSuspendedStateList().get(i)));
            if ((i + 1) < processingExecuterPersistenceContainer.getSuspendedStateList().size()) {
                builder.append(TextUtil.NL);
            }
        }

        return builder.toString();
    }
}
