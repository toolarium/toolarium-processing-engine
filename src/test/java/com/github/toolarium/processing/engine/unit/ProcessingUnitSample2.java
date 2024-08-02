/*
 * ProcessingUnitSample2.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.unit;

import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;


/**
 * Implements a simple processing unit
 *   
 * @author patrick
 */
public class ProcessingUnitSample2 extends AbstractProcessingUnitImpl {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final  ParameterDefinition INPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("inputFilename").isMandatory().description("The filename incl. path to read in a file.").build();

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        // check how many entries we have to process, e.g. counting database records to process
        // it will be called just once, the first time before start processing
        // this number will be set in getProcessingProgress().setNumberOfUnitsToProcess(...) 
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(10);
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {
        
        // This is the main part where the processing takes place

        // In case of successful processing
        processingUnitStatusBuilder.increaseNumberOfSuccessfulUnits();
        
        // other wise if it was failed
        //processingUnitStatusBuilder.increaseNumberOfFailedUnits();

        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR. Additional a message can be set
        //processingUnitStatusBuilder.warn("Warning sample");
        //processingUnitStatusBuilder.error("Error sample");
        //processingUnitStatusBuilder.message("Error sample");

        // Support of statistic:
        //processingUnitStatusBuilder.statistic("counter", 1);

        ThreadUtil.getInstance().sleep(50L);

        return processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits().build();        
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    /* In case we have to release any resources
    @Override
    public void releaseResource() throws ProcessingException {
    }
    */
}
