/*
 * ProcessingUnitSample2.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.unit;

import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.exception.ProcessingException;

/**
 * Implements a simple processing unit
 *   
 * @author patrick
 */
public class ProcessingUnitSample2 extends AbstractProcessingUnitImpl {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final  ParameterDefinition INPUT_FILENAME_PARAMETER = 
            new ParameterDefinition("inputFilename", ParameterValueType.STRING,
                                    ParameterDefinition.NO_DEFAULT_PARAMETER, ParameterDefinition.NOT_OPTIONAL, 1,
                                    ParameterDefinition.EMPTY_VALUE_NOT_ALLOWED, "The filename incl. path to read in a file.");

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#countNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    protected long countNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) {
        // check how many entries we have to process, e.g. counting database records to process
        // it will be called just once, the first time before start processing
        // this number will be set in getProcessingProgress().setNumberOfUnitsToProcess(...) 
        return 10;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) throws ProcessingException {
        
        // This is the main part where the processing takes place
        
        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR can be set
        //getProcessingProgress().setStatusMessage("Warning sample");
        //getProcessingProgress().setProcessingRuntimeStatus(ProcessingRuntimeStatus.WARN);

        // Support of additional statistic:
        //getProcessingProgress().addStatistic("counter", 1d);

        ThreadUtil.getInstance().sleep(10L);
        
        // Increase the number of processed units
        getProcessingProgress().increaseNumberOfProcessedUnits();
        
        // If it was failed you can increase the number of failed units
        //getProcessingProgress().increaseNumberOfFailedUnits();
        
        // It is called as long as getProcessStatus().setHasNext is set to false.
        getProcessStatus().setHasNext(getProcessingProgress().getNumberOfUnprocessedUnits() > 0);
        return getProcessStatus();
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
