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
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingUnitReference;
import com.github.toolarium.processing.engine.impl.executer.impl.ProcessingExecuterImpl;
import com.github.toolarium.processing.engine.listener.LogProcessingListener;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test the processing executer
 * 
 * @author patrick
 */
public class TestProcessingExecuter {

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
            
            .newProcessingUnit()
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
                
            .newProcessingUnit()
            .id("45678")
            .name("name2")
            .processingUnitClass(ProcessingUnitSample.class.getName()) // string sample
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2"))
                
            .build();
        
        // get a processing executer
        IProcessingExecuter processingExecuter = new ProcessingExecuterImpl().setProcessingExecuterListener(new LogProcessingListener());
        IProcessingUnitContext processingUnitContext = null;
        
        // execute
        processingExecuter.execute(list, processingUnitContext);
        
        while (processingExecuter.getStatus().getNumberOfRunningProcessings() > 0) {
            ThreadUtil.getInstance().sleep(20L);
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
                
            .newProcessingUnit()
            .id("45678")
            .name("name2")
            .processingUnitClass(ProcessingUnitSample.class)
            .parameter(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2"))
                
            .build();
        
        // get a processing executer
        IProcessingExecuter processingExecuter = new ProcessingExecuterImpl().setProcessingExecuterListener(new LogProcessingListener());
        IProcessingUnitContext processingUnitContext = null;
        
        // execute
        processingExecuter.execute(list, processingUnitContext);
        ThreadUtil.getInstance().sleep(10L);

        // suspend
        ProcessingExecuterPersistenceContainer processingExecuterSuspendedContent = processingExecuter.shutdown();
        assertNotNull(processingExecuterSuspendedContent.getSuspendedStateList());
        assertFalse(processingExecuterSuspendedContent.getSuspendedStateList().isEmpty());
        assertTrue(processingExecuterSuspendedContent.getSuspendedStateList().size() >= 0 && processingExecuterSuspendedContent.getSuspendedStateList().size() <= 2);

        // resume
        processingExecuter = new ProcessingExecuterImpl().setProcessingExecuterListener(new LogProcessingListener());
        processingExecuter.execute(processingExecuterSuspendedContent);
        while (processingExecuter.getStatus().getNumberOfRunningProcessings() > 0) {
            ThreadUtil.getInstance().sleep(500L);
        }
        
        processingExecuterSuspendedContent = processingExecuter.shutdown();
        assertNotNull(processingExecuterSuspendedContent.getSuspendedStateList());
        assertTrue(processingExecuterSuspendedContent.getSuspendedStateList().isEmpty());
    }
}
