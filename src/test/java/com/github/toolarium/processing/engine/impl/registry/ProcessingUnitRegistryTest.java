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

import com.github.toolarium.processing.engine.dto.IProcessingUnit;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample;
import com.github.toolarium.processing.engine.unit.ProcessingUnitSample2;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link ProcessingUnitRegistry}.
 *  
 * @author patrick
 */
public class ProcessingUnitRegistryTest {

    /**
     * Test register and unregister
     */
    @Test
    public void test() {
        assertFalse(ProcessingUnitRegistry.getInstance().isRegistered(ProcessingUnitSample.class.getName()));

        IProcessingUnit processingUnit = ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample.class);
        assertEquals(processingUnit.getName(), ProcessingUnitSample.class.getName());
        assertEquals("[ParameterDefinition [key=inputFilename, valueDataType=STRING, defaultValue=null, isOptional=false, "
                     + "minOccurs=1, maxOccurs=1, isEmptyValueAllowed=false, hasValueToProtect=false, "
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
        IProcessingUnit processingUnit = ProcessingUnitRegistry.getInstance().register(ProcessingUnitSample.class);
        com.github.toolarium.processing.unit.IProcessingUnit processingUnitInstance = ProcessingUnitRegistry.getInstance().newInstance(processingUnit.getName());
        assertNotNull(processingUnitInstance);
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
        
        assertEquals(ProcessingUnitSample.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("ProcessingUnitSample").iterator().next().getProcessingUnit().getName());
        assertEquals(ProcessingUnitSample.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("P*").iterator().next().getProcessingUnit().getName());
        assertEquals(ProcessingUnitSample2.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("ProcessingUnitSample2").iterator().next().getProcessingUnit().getName());
        assertEquals(ProcessingUnitSample2.class.getName(), ProcessingUnitRegistry.getInstance().searchProcessingUnitList("P*2").iterator().next().getProcessingUnit().getName());

        ProcessingUnitRegistry.getInstance().unregister(ProcessingUnitSample.class.getName());
        ProcessingUnitRegistry.getInstance().unregister(ProcessingUnitSample2.class.getName());
    }
}
