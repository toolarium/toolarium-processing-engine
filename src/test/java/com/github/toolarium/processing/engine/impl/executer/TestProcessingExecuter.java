/*
 * TestProcessingExecuter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.dto.IProcessingResult;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingUnitReference;
import com.github.toolarium.processing.engine.impl.executer.impl.ProcessingExecuterImpl;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test the processing executer
 * 
 * @author patrick
 */
public class TestProcessingExecuter {
    private static final Logger LOG = LoggerFactory.getLogger(TestProcessingExecuter.class);

    /**
     * Class not found
     */
    @Test
    public void testInvalidClass() {
        assertThrows(ClassNotFoundException.class, () -> {  // ClassNotFoundException exception = 
            new ProcessingExecutionBuilder().processingUnitClass("my class");
        });
        
    }

    
    /**
     * Test builder
     *
     * @throws ClassNotFoundException In case the class can't be found
     */
    @Test
    public void testBuilder() throws ClassNotFoundException {
        List<ProcessingUnitReference> list = new ProcessingExecutionBuilder()
            .id("1234")
            .name("name1")
            .processingUnitClass(ProcessingUnitSample.class)
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename1"))
            
            .chain()
            .id("45678")
            .name("name2")
            .processingUnitClass(ProcessingUnitSample.class.getName()) // string sample
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2"))
            
            .build();

        String className = ProcessingUnitSample.class.getName();
        String ref = "[ProcessingUnitReference [id=1234, name=name1, processingUnitClass=" + className + ", parameterList=[Parameter [key=inputFilename, value=ParameterValueList [valueList=[my-filename1]]]]], "
                   + "ProcessingUnitReference [id=45678, name=name2, processingUnitClass=" + className + ", parameterList=[Parameter [key=inputFilename, value=ParameterValueList [valueList=[my-filename2]]]]]]";
        assertEquals(ref, list.toString());
    }
    


    /**
     * Test builder
     *
     * @throws ClassNotFoundException In case the class can't be found
     */
    @Test
    public void test() throws ClassNotFoundException {
        List<ProcessingUnitReference> list = new ProcessingExecutionBuilder()
            .id("1234")
            .name("name1")
            .processingUnitClass(ProcessingUnitSample.class)
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename1"))
                
            .chain()
            .id("45678")
            .name("name2")
            .processingUnitClass(ProcessingUnitSample.class.getName()) // string sample
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2"))
                
            .build();
        
        // get a processing executer
        IProcessingExecuter processingExecuter = new ProcessingExecuterImpl().setProcessingExecuterListener(new ProcessingListener());
        IProcessingUnitContext processingUnitContext = null;
        
        // execute
        processingExecuter.execute(list, processingUnitContext);
        
        while (processingExecuter.getStatus().getNumberOfRunningProcessings() > 0) {
            LOG.debug("WAIT");
            ThreadUtil.getInstance().sleep(10L);
        }
        
        ProcessingExecuterPersistenceContainer processingExecuterSuspendedContent = processingExecuter.shutdown();
        assertNotNull(processingExecuterSuspendedContent.getSuspendedStateList());
        assertTrue(processingExecuterSuspendedContent.getSuspendedStateList().isEmpty());
    }

    
    /**
     * Test suspend resume
     */
    @Test
    public void testSuspendResume() {
        List<ProcessingUnitReference> list = new ProcessingExecutionBuilder()
            .id("1234")
            .name("name1")
            .processingUnitClass(ProcessingUnitSample.class)
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename1"))
                
            .chain()
            .id("45678")
            .name("name2")
            .processingUnitClass(ProcessingUnitSample.class)
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2"))
                
            .build();
        
        // get a processing executer
        IProcessingExecuter processingExecuter = new ProcessingExecuterImpl().setProcessingExecuterListener(new ProcessingListener());
        IProcessingUnitContext processingUnitContext = null;
        
        // execute
        processingExecuter.execute(list, processingUnitContext);
        ThreadUtil.getInstance().sleep(10L);

        // suspend
        ProcessingExecuterPersistenceContainer processingExecuterSuspendedContent = processingExecuter.shutdown();
        assertNotNull(processingExecuterSuspendedContent.getSuspendedStateList());
        assertFalse(processingExecuterSuspendedContent.getSuspendedStateList().isEmpty());
        //assertEquals(2, processingExecuterSuspendedContent.getSuspendedStateList().size());

        // resume
        processingExecuter = new ProcessingExecuterImpl().setProcessingExecuterListener(new ProcessingListener());
        processingExecuter.execute(processingExecuterSuspendedContent);
        while (processingExecuter.getStatus().getNumberOfRunningProcessings() > 0) {
            LOG.debug("WAIT");
            ThreadUtil.getInstance().sleep(500L);
        }
        
        processingExecuterSuspendedContent = processingExecuter.shutdown();
        assertNotNull(processingExecuterSuspendedContent.getSuspendedStateList());
        assertTrue(processingExecuterSuspendedContent.getSuspendedStateList().isEmpty());
    }

    
    /**
     * Implements a processing executer listener
     * 
     * @author patrick
     */
    class ProcessingListener implements IProcessingListener {

        /**
         * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessEnd(com.github.toolarium.processing.engine.dto.IProcessingResult)
         */
        @Override
        public void notifyProcessEnd(IProcessingResult processingResult) {
            StringBuilder builder = new StringBuilder();
            builder.append(ProcessingUnitUtil.getInstance().preapre(processingResult.getId(), processingResult.getName(), null));
            builder.append(" - #").append(processingResult.getNumberOfProcessedUnits())
                   .append(" (").append(processingResult.getNumberOfSuccessfulUnits()).append("/").append(processingResult.getNumberOfFailedUnits()).append("), ");
            builder.append(" - ").append(processingResult.getStartTimestamp()).append(" - ").append(processingResult.getStopTimestamp()).append(" (").append(processingResult.getProcessingDuration()).append("ms)");
            builder.append(": ").append(processingResult.getProcessingRuntimeStatus());

            LOG.info("=>" + processingResult.toString());
            
            //String getInstance();
            //boolean isAborted();
            //List<String> getStatusMessageList();
            //IProcessingStatistic getProcesingStatistic();
            
        }


        /**
         * @see com.github.toolarium.processing.engine.IProcessingListener#notifyProcessingUnitStateChange(java.lang.String, java.lang.String, java.lang.String, com.github.toolarium.processing.unit.dto.ProcessingActionStatus, 
         * com.github.toolarium.processing.unit.IProcessingUnitContext, com.github.toolarium.processing.unit.IProcessingProgress)
         */
        @Override
        public void notifyProcessingUnitStateChange(String id, String name, String processingUnitClass, ProcessingActionStatus processingActionStatus, IProcessingUnitContext processingUnitContext, IProcessingProgress processingProgress) {
            
        }
    }
}
