/*
 * ProcessingUnitRegistry.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.registry;

import com.github.toolarium.common.util.ClassInstanceUtil;
import com.github.toolarium.processing.engine.IProcessingUnitRegistry;
import com.github.toolarium.processing.engine.dto.parameter.IParameterDefinition;
import com.github.toolarium.processing.engine.dto.parameter.ParameterDefinition;
import com.github.toolarium.processing.engine.dto.unit.IProcessingUnitDefinition;
import com.github.toolarium.processing.engine.dto.unit.ProcessingUnitDefinition;
import com.github.toolarium.processing.engine.exception.ValidationException;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IProcessingUnitRegistry}.
 *  
 * @author patrick
 */
public final class ProcessingUnitRegistry implements IProcessingUnitRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingUnitRegistry.class);
    private Map<String, IProcessingUnitDefinition> processingUnitMap;

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ProcessingUnitRegistry INSTANCE = new ProcessingUnitRegistry();
    }

    
    /**
     * Constructor
     */
    private ProcessingUnitRegistry() {
        processingUnitMap = new ConcurrentHashMap<String, IProcessingUnitDefinition>();
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ProcessingUnitRegistry getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#register(java.lang.Class)
     */
    @Override
    public IProcessingUnitDefinition register(Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        if (processingUnitClass == null) {
            throw new ValidationException("Invalid empty proccessing unit class!");
        }

        if (isRegistered(processingUnitClass)) {
            LOG.debug("The processing class " + processingUnitClass.getName() + " is already registered!");
            return processingUnitMap.get(processingUnitClass.getName());
        }
        
        try {
            List<IParameterDefinition> list = validateProcessingUnit(processingUnitClass);

            // register
            LOG.info("Register processing unit " + processingUnitClass + ".");
            IProcessingUnitDefinition processingUnit = new ProcessingUnitDefinition(processingUnitClass, list);
            processingUnitMap.put(processingUnitClass.getName(), processingUnit);
            return processingUnit;
        } catch (RuntimeException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not register processing unit " + processingUnitClass.getName() + ": " + e.getMessage(), e);
            }

            throw new ValidationException("Could not register processing unit " + processingUnitClass.getName() + ": " + e.getMessage(), e);
        }
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#register(java.lang.String)
     */
    @Override
    public IProcessingUnitDefinition register(String processingUnitClassName) throws ValidationException {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit class name!");
        }
        
        try {
            return register(resolveProcessingUnitClass(processingUnitClassName));
        } catch (RuntimeException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not register processing unit " + processingUnitClassName + ": " + e.getMessage(), e);
            }
            
            throw new ValidationException("Could not register processing unit " + processingUnitClassName + ": " + e.getMessage(), e);
        }
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#unregister(java.lang.Class)
     */
    @Override
    public IProcessingUnitDefinition unregister(Class<? extends IProcessingUnit> processingUnit) throws ValidationException {
        if (processingUnit == null) {
            throw new ValidationException("Invalid empty proccessing unit class!");
        }
        
        return unregister(processingUnit.getName());
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#unregister(java.lang.String)
     */
    @Override
    public IProcessingUnitDefinition unregister(String processingUnitClassName) throws ValidationException {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit class name!");
        }
        
        LOG.info("Unregister processing unit " + processingUnitClassName + ".");
        return processingUnitMap.remove(processingUnitClassName);
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#isRegistered(java.lang.Class)
     */
    @Override
    public boolean isRegistered(Class<? extends IProcessingUnit> processingUnit) {
        if (processingUnit == null) {
            throw new ValidationException("Invalid empty proccessing unit class!");
        }
        
        return isRegistered(processingUnit.getName());
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#isRegistered(java.lang.String)
     */
    @Override
    public boolean isRegistered(String processingUnitClassName) {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit class name!");
        }
        
        return processingUnitMap.containsKey(processingUnitClassName);
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#getProcessingUnitList(java.lang.String)
     */
    @Override
    public IProcessingUnitDefinition getProcessingUnitList(String processingUnitClassName) {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit class name!");
        }

        return processingUnitMap.get(processingUnitClassName);
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#searchProcessingUnitList(java.lang.String)
     */
    @Override
    public Set<IProcessingUnitDefinition> searchProcessingUnitList(String filterName) {
        String filter = "^.*$";
        if (filterName != null && !filterName.isBlank()) {
            // don't allow regular repression
            String name = filterName.trim()
                    .replace("%", "")
                    .replace("^", "")
                    .replace("$", "")
                    .replace("+", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("[", "")
                    .replace(".", "\\.");                    
            filter = "^.*" + name + ".*$";
        }

        Set<IProcessingUnitDefinition> resultList = new TreeSet<IProcessingUnitDefinition>();
        for (Map.Entry<String, IProcessingUnitDefinition> e : processingUnitMap.entrySet()) {
            if (Pattern.matches(filter, e.getValue().getProcessingClassname())) {
                resultList.add(e.getValue());
            }
        }

        return resultList;
    }

    
    /**
     * Resolve processing unit class
     *  
     * @param processingUnitClassName the processing unit class name
     * @return the processing unit class
     * @throws ValidationException In case of invalid parameters
     */
    public Class<IProcessingUnit> resolveProcessingUnitClass(String processingUnitClassName) throws ValidationException {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty processing unit class name!");
        }

        try {
            return ClassInstanceUtil.getInstance().getClassObject(processingUnitClassName);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not create processing unit [" + processingUnitClassName + "]: " + e.getMessage(), e);
            }
            
            throw new ValidationException("Could not create processing unit [" + processingUnitClassName + "]: " + e.getMessage(), e);
        }
    }

    
    /**
     * Validate the processing unit
     * 
     * @param processingUnitClass the class to validate
     * @return the parameters
     * @throws ValidationException In case of invalid parameters
     */
    private List<IParameterDefinition> validateProcessingUnit(Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        try {
            final String id = "REGISTER";
            IProcessingUnit processingUnitInstance = ProcessingUnitUtil.getInstance().createProcessingUnitInstance(id, null, processingUnitClass);
    
            // get parameter definitions
            List<IParameterDefinition> list = new ArrayList<IParameterDefinition>();
            for (com.github.toolarium.processing.unit.dto.ParameterDefinition parameterDefinition : processingUnitInstance.getParameterDefinition()) {
                list.add(new ParameterDefinition(parameterDefinition));
            }
    
            ProcessingUnitUtil.getInstance().releaseResource(id, null, processingUnitInstance);
            return list;
        } catch (Exception e) {
            throw new ValidationException("Could not register processing unit " + processingUnitClass + ": " + e.getMessage(), e);
        }
    }
}
