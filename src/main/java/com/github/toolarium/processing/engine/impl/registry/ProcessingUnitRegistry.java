/*
 * ProcessingUnitRegistry.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.registry;

import com.github.toolarium.common.util.ClassInstanceUtil;
import com.github.toolarium.processing.engine.IProcessingUnitRegistry;
import com.github.toolarium.processing.engine.dto.IParameterDefinition;
import com.github.toolarium.processing.engine.dto.IProcessingUnit;
import com.github.toolarium.processing.engine.dto.ParameterDefinition;
import com.github.toolarium.processing.engine.dto.ProcessingUnit;
import com.github.toolarium.processing.engine.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private Map<String, ProcessingUnitHolder> processingUnitMap;

    
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
        processingUnitMap = new ConcurrentHashMap<String, ProcessingUnitHolder>();
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
    public IProcessingUnit register(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnitClass) throws ValidationException {
        if (processingUnitClass == null) {
            throw new ValidationException("Invalid empty proccessing unit class!");
        }

        if (isRegistered(processingUnitClass)) {
            LOG.debug("The processing class " + processingUnitClass.getName() + " is already registered!");
            return processingUnitMap.get(processingUnitClass.getName()).getProcessingUnit();
        }
        
        try {
            com.github.toolarium.processing.unit.IProcessingUnit processingUnitInstance = createProcessingUnitInstance(processingUnitClass);

            // get parameter definitions
            List<IParameterDefinition> list = new ArrayList<IParameterDefinition>();
            for (com.github.toolarium.processing.unit.dto.ParameterDefinition parameterDefinition : processingUnitInstance.getParameterDefinition()) {
                list.add(new ParameterDefinition(parameterDefinition));
            }
            
            // register
            LOG.info("Register processing unit " + processingUnitClass + ".");
            IProcessingUnit processingUnit = new ProcessingUnit(processingUnitClass.getName(), list);
            processingUnitMap.put(processingUnitClass.getName(), new ProcessingUnitHolder().setProcessingUnit(processingUnit).setProcessingUnitClass(processingUnitClass));
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
    public IProcessingUnit register(String processingUnitClassName) throws ValidationException {
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
    public IProcessingUnit unregister(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnit) throws ValidationException {
        if (processingUnit == null) {
            throw new ValidationException("Invalid empty proccessing unit class!");
        }
        
        return unregister(processingUnit.getName());
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#unregister(java.lang.String)
     */
    @Override
    public IProcessingUnit unregister(String processingUnitClassName) throws ValidationException {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit class name!");
        }
        
        LOG.info("Unregister processing unit " + processingUnitClassName + ".");
        return processingUnitMap.remove(processingUnitClassName).getProcessingUnit();
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#isRegistered(java.lang.Class)
     */
    @Override
    public boolean isRegistered(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnit) {
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
    public IProcessingUnitReference getProcessingUnitList(String processingUnitClassName) {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty proccessing unit class name!");
        }

        return processingUnitMap.get(processingUnitClassName);
    }

    
    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#searchProcessingUnitList(java.lang.String)
     */
    @Override
    public Set<IProcessingUnitReference> searchProcessingUnitList(String filterName) {
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

        Set<IProcessingUnitReference> resultList = new TreeSet<IProcessingUnitReference>();
        for (Map.Entry<String, ProcessingUnitHolder> e : processingUnitMap.entrySet()) {
            if (Pattern.matches(filter, e.getValue().getProcessingUnit().getName())) {
                resultList.add(e.getValue());
            }
        }

        return resultList;
    }


    /**
     * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry#newInstance(java.lang.String)
     */
    @Override
    public com.github.toolarium.processing.unit.IProcessingUnit newInstance(String processingUnitClassName) throws ValidationException {
        if (processingUnitClassName == null || processingUnitClassName.isBlank()) {
            throw new ValidationException("Invalid empty processing unit class name!");
        }
        
        return createProcessingUnitInstance(resolveProcessingUnitClass(processingUnitClassName));
    }

    
    /**
     * Resolve processing unit class
     *  
     * @param processingUnitClassName the processing unit class name
     * @return the processing unit class
     * @throws ValidationException In case of invalid parameters
     */
    private Class<com.github.toolarium.processing.unit.IProcessingUnit> resolveProcessingUnitClass(String processingUnitClassName) throws ValidationException {
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
     * Create the processing unit instance
     * 
     * @param processingUnitClass the processing class
     * @return the processing instance
     * @throws ValidationException In case of invalid parameters
     */
    private com.github.toolarium.processing.unit.IProcessingUnit createProcessingUnitInstance(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnitClass) throws ValidationException {
        if (processingUnitClass == null) {
            throw new ValidationException("Invalid empty class!");
        }
        
        try {
            com.github.toolarium.processing.unit.IProcessingUnit processingUnit = ClassInstanceUtil.getInstance().newInstance(processingUnitClass);
            return processingUnit;
        } catch (Exception e) {
            throw new ValidationException("Could not register processing unit " + processingUnitClass + ": " + e.getMessage(), e);
        }
    }


    /**
     * Defines the processing unit holder
     * 
     * @author patrick
     */
    class ProcessingUnitHolder implements IProcessingUnitReference, Comparable<ProcessingUnitHolder> {
        private IProcessingUnit processingUnit;
        private Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnitClass;
        

        /**
         * Constructor for ProcessingUnitHolder
         */
        ProcessingUnitHolder() {
        }
        

        /**
         * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry.IProcessingUnitReference#getProcessingUnit()
         */
        @Override
        public IProcessingUnit getProcessingUnit() {
            return processingUnit;
        }

        
        /**
         * Set the processing unit
         *
         * @param processingUnit the processing unit
         * @return this instance
         */
        public ProcessingUnitHolder setProcessingUnit(IProcessingUnit processingUnit) {
            this.processingUnit = processingUnit;
            return this;
        }


        /**
         * @see com.github.toolarium.processing.engine.IProcessingUnitRegistry.IProcessingUnitReference#getProcessingUnitClass()
         */
        @Override
        public Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> getProcessingUnitClass() {
            return processingUnitClass;
        }
        
        
        /**
         * Set the processing unit class
         *
         * @param processingUnitClass the processing unit class
         * @return this instance
         */
        public ProcessingUnitHolder setProcessingUnitClass(Class<? extends com.github.toolarium.processing.unit.IProcessingUnit> processingUnitClass) {
            this.processingUnitClass = processingUnitClass;
            return this;
        }


        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + Objects.hash(processingUnit, processingUnitClass);
            return result;
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            
            if (obj == null) {
                return false;
            }
            
            if (getClass() != obj.getClass()) {
                return false;
            }
            
            ProcessingUnitHolder other = (ProcessingUnitHolder) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
                return false;
            }
            return Objects.equals(processingUnit, other.processingUnit) && Objects.equals(processingUnitClass, other.processingUnitClass);
        }

        
        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(ProcessingUnitHolder o) {
            return processingUnit.getName().compareTo(o.getProcessingUnit().getName());
        }


        /**
         * Enclosing class
         *
         * @return the class
         */
        private ProcessingUnitRegistry getEnclosingInstance() {
            return ProcessingUnitRegistry.this;
        }
    }
}
