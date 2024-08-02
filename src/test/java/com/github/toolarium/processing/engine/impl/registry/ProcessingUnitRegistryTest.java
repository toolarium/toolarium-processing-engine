/*
 * ProcessingUnitRegistryTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.processing.engine.ProcessingEngineFactory;
import com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample2;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitInstanceManager;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test the {@link ProcessingUnitRegistry}.
 *  
 * @author patrick
 */
public class ProcessingUnitRegistryTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingUnitRegistryTest.class);

    /**
     * Test register and unregister
     */
    @Test
    public void test() {
        assertFalse(ProcessingUnitRegistry.getInstance().isRegistered(ProcessingUnitSample.class.getName()));

        IProcessingUnitDefinition processingUnit = ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample.class);
        assertEquals(processingUnit.getProcessingClassname(), ProcessingUnitSample.class.getName());
        assertEquals("[ParameterDefinition [key=inputFilename, valueDataType=STRING, defaultValue=null, isOptional=false, "
                     + "minOccurs=1, maxOccurs=1, isEmptyValueAllowed=true, hasValueToProtect=false, "
                     + "description=The filename incl. path to read in a file.]]", 
                     processingUnit.getParameterDefinitionList().toString());
        
        assertTrue(ProcessingUnitRegistry.getInstance().isRegistered(ProcessingUnitSample.class));
        assertTrue(ProcessingUnitRegistry.getInstance().isRegistered(ProcessingUnitSample.class.getName()));
        ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample.class.getName());

        ProcessingUnitRegistry.getInstance().unregister(ProcessingUnitSample.class.getName());
        assertFalse(ProcessingUnitRegistry.getInstance().isRegistered(ProcessingUnitSample.class.getName()));
    }


    /**
     * Test new instance
     */
    @Test
    public void testInstance() {
        IProcessingUnitInstanceManager instanceManager = new TestProcessingUnitInstanceManager();
        ProcessingEngineFactory.getInstance().setProcessingUnitInstanceManager(instanceManager);
        
        // register
        IProcessingUnitDefinition processingUnit = ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample.class);
        
        IProcessingUnit processingUnitInstance = ProcessingUnitUtil.getInstance().createProcessingUnitInstance("id", "name", processingUnit.getProcessingClass());
        assertNotNull(processingUnitInstance);
        ProcessingUnitUtil.getInstance().releaseResource("id", "name", processingUnitInstance);

        // unregister
        ProcessingUnitRegistry.getInstance().unregister(ProcessingUnitSample.class.getName());
    }
    
    
    /**
     * Test search
     */
    @Test
    public void search() {
        ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample.class);
        ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample2.class);
        assertEquals(2, ProcessingUnitRegistry.getInstance().searchProcessingUnitList("ProcessingUnitSample").size());
        assertEquals(2, ProcessingUnitRegistry.getInstance().searchProcessingUnitList("ProcessingUnitSample*").size());
        assertEquals(2, ProcessingUnitRegistry.getInstance().searchProcessingUnitList("Pr*").size());
        assertEquals(1, ProcessingUnitRegistry.getInstance().searchProcessingUnitList(ProcessingUnitSample2.class.getName()).size());
        assertEquals(2, ProcessingUnitRegistry.getInstance().searchProcessingUnitList(ProcessingUnitSample.class.getName()).size());
        
        assertEquals(ProcessingUnitSample.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("ProcessingUnitSample").iterator().next().getProcessingClassname());
        assertEquals(ProcessingUnitSample.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("P*").iterator().next().getProcessingClassname());
        assertEquals(ProcessingUnitSample2.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("ProcessingUnitSample2").iterator().next().getProcessingClassname());
        assertEquals(ProcessingUnitSample2.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("P*2").iterator().next().getProcessingClassname());

        ProcessingUnitRegistry.getInstance().unregister(ProcessingUnitSample.class.getName());
        ProcessingUnitRegistry.getInstance().unregister(ProcessingUnitSample2.class.getName());
    }
    
    
    class TestProcessingUnitInstanceManager extends ProcessingUnitInstanceManager {
        /**
         * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager#createProcessingUnitInstance(java.lang.String, java.lang.String, java.lang.Class)
         */
        @Override
        public IProcessingUnit createProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
            IProcessingUnit instance = super.createProcessingUnitInstance(id, name, processingUnitClass);
            LOG.info(ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass) + " new instance");
            return instance;
        }


        /**
         * @see com.github.toolarium.processing.unit.runtime.ProcessingUnitInstanceManager#createParallelProcessingUnitInstance(java.lang.String, java.lang.String, java.lang.Class)
         */
        @Override
        public IProcessingUnit createParallelProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
            IProcessingUnit instance = super.createParallelProcessingUnitInstance(id, name, processingUnitClass);
            LOG.info(ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass) + " new instance");
            return instance;
        }
        

        /**
         * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager#releaseResource(java.lang.String, java.lang.String, com.github.toolarium.processing.unit.IProcessingUnit)
         */
        @Override
        public void releaseResource(String id, String name, IProcessingUnit processingUnit) {
            LOG.info(ProcessingUnitUtil.getInstance().toString(id, name, processingUnit.getClass()) + " release instance");
            super.releaseResource(id, name, processingUnit);
        }
    }
}
