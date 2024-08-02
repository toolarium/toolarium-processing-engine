/*
 * ProcessingEngineTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.common.util.TextUtil;
import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition;
import com.github.toolarium.processing.engine.impl.util.ProcessingPersistenceUtil;
import com.github.toolarium.processing.engine.listener.LogProcessingListener;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample2;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test the processing engine
 * 
 * @author patrick
 */
public class ProcessingEngineTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingEngineTest.class);
    
    
    /**
     * Tets
     */
    @Test
    public void test() {
        IProcessEngine processEngine = ProcessingEngineFactory.getInstance().getProcessingEngine();
        processEngine.addListener(new LogProcessingListener());

        // register processing
        IProcessingUnitDefinition p1 =  processEngine.getProcessingUnitRegistry().register(ProcessingUnitSample.class);
        LOG.debug("Parameters:" + p1.getParameterDefinitionList());
        IProcessingUnitDefinition p2 = processEngine.getProcessingUnitRegistry().register(ProcessingUnitSample2.class.getName());
        LOG.debug("Parameters:" + p2.getParameterDefinitionList());

        // start processing
        IProcessingUnitRunnable runnable1 = processEngine.execute(UUID.randomUUID().toString(), "test1", p1.getProcessingClassname(),
                                                                  List.of(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename1")));
        assertNotNull(runnable1);

        IProcessingUnitRunnable runnable2 = processEngine.execute(UUID.randomUUID().toString(), "test2", p2.getProcessingClassname(),
                                                                  List.of(new Parameter(ProcessingUnitSample2.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2")));
        assertNotNull(runnable2);

        
        // wait processing
        while (processEngine.getStatus().getNumberOfRunningProcessings() > 0) {
            LOG.debug("WAIT");
            ThreadUtil.getInstance().sleep(500L);
        }
        
        // shutdown processing
        byte[] persistedContent = processEngine.shutdown();
        assertNull(persistedContent); // if there were no processings it is null!
    }


    /**
     * Tets
     */
    @Test
    public void testSuspendAndResume() {
        IProcessEngine processEngine = ProcessingEngineFactory.getInstance().getProcessingEngine();
        processEngine.addListener(new LogProcessingListener());

        // register processing
        IProcessingUnitDefinition p1 = processEngine.getProcessingUnitRegistry().register(ProcessingUnitSample.class);
        IProcessingUnitDefinition p2 = processEngine.getProcessingUnitRegistry().register(ProcessingUnitSample2.class.getName());

        // start processing
        processEngine.execute(UUID.randomUUID().toString(), "test1", p1.getProcessingClassname(),
                              List.of(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename1")));

        processEngine.execute(UUID.randomUUID().toString(), "test2", p2.getProcessingClassname(),
                              List.of(new Parameter(ProcessingUnitSample2.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2")));

        ThreadUtil.getInstance().sleep(10L);
        
        // suspend all processings
        byte[] persistedContent = processEngine.shutdown();
        assertNotNull(persistedContent); 
        
        LOG.info("Persisted state:" + TextUtil.NL + ProcessingPersistenceUtil.getInstance().toString(persistedContent));
        processEngine = null;
        
        LOG.info("Resume...");
        processEngine = ProcessingEngineFactory.getInstance().getProcessingEngine(persistedContent);
        processEngine.addListener(new LogProcessingListener());
        
        // wait processing
        while (processEngine.getStatus().getNumberOfRunningProcessings() > 0) {
            LOG.debug("WAIT");
            ThreadUtil.getInstance().sleep(500L);
        }
        
        // shutdown processing
        persistedContent = processEngine.shutdown();
        assertNull(persistedContent); // if there were no processings it is null!
    }
}
