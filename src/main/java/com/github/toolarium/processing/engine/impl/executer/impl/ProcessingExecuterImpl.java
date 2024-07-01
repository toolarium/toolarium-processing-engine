/*
 * ProcessingExecuterImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.impl.executer.impl;

import com.github.toolarium.common.statistic.StatisticCounter;
import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.engine.IProcessingListener;
import com.github.toolarium.processing.engine.dto.result.IProcessingStatistic;
import com.github.toolarium.processing.engine.dto.result.ProcessingResult;
import com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter;
import com.github.toolarium.processing.engine.impl.executer.ProcessingExecutionBuilder;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer;
import com.github.toolarium.processing.engine.impl.executer.dto.ProcessingUnitReference;
import com.github.toolarium.processing.engine.impl.status.IProcessingExecuterStatus;
import com.github.toolarium.processing.engine.impl.status.ProcessingExecuterStatus;
import com.github.toolarium.processing.engine.impl.util.ProcessingThreadFactory;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitContext;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener;
import com.github.toolarium.processing.unit.runtime.runnable.impl.ProcessingUnitRunnable;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IProcessingExecuter}.
 * 
 * @author patrick
 */
public class ProcessingExecuterImpl implements IProcessingExecuter, IProcessingUnitRunnableListener {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingExecuterImpl.class);
    private int corePoolSize = 0;
    private int maximumPoolSize = Integer.MAX_VALUE;
    private long keepAliveTimeInSeconds = 60L;
    private Map<String, ProcessingUnitRunnable> processingUnitRunnableMap;
    private IProcessingListener processingListener;
    private ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer;
    private ThreadPoolExecutor threadPoolExecutor;
    //private Executor cleanupExecutor = Executors.newSingleThreadExecutor();

    
    /**
     * Constructor for ProcessingExecuterImpl
     */
    public ProcessingExecuterImpl() {
        processingUnitRunnableMap = new ConcurrentHashMap<String, ProcessingUnitRunnable>();        
        processingExecuterPersistenceContainer = null;
        processingListener = null;
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTimeInSeconds, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        threadPoolExecutor.setThreadFactory(ProcessingThreadFactory.getInstance());
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            
            /**
             * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
             */
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            }
        });

        /*
        cleanupExecutor = Executors.newSingleThreadExecutor(ProcessingThreadFactory.getInstance());
        cleanupExecutor.execute(new Runnable() {
         
            @Override
            public void run() {
                boolean run = true;
                
                while (run) {
                    if (processingUnitRunnableMap != null && !processingUnitRunnableMap.isEmpty()) {
                        for (IProcessingUnitRunnable processingUnitRunnable : processingUnitRunnableMap.values()) {
                            if (processingUnitRunnable.getProcessStatus() != null) {
                                if (processingUnitRunnable.getProcessStatus().hasNext()) {
                                    // TODO
                                } else if (processingUnitRunnable.getProcessStatus().getProcessingProgress() != null) {
                                    IProcessStatus processingStatus = processingUnitRunnable.getProcessStatus();
                                    if (processingStatus != null) {
                                        // TODO
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });  
        */
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#execute(java.lang.Class, java.util.List)
     */
    @Override
    public IProcessingUnitRunnable execute(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList) {
        return execute(null, null, processingUnitClass, parameterList);
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#execute(java.lang.String, java.lang.String, java.lang.Class, java.util.List)
     */
    @Override
    public IProcessingUnitRunnable execute(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList) {
        return execute(id, name, processingUnitClass, parameterList, null);
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#execute(java.lang.String, java.lang.String, java.lang.Class, java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessingUnitRunnable execute(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) {
        List<IProcessingUnitRunnable> list = execute(new ProcessingExecutionBuilder().id(id).name(name).processingUnitClass(processingUnitClass).parameters(parameterList.toArray(Parameter[]::new)).build(), processingUnitContext);
        
        if (!list.isEmpty()) {
            return list.get(0);
        }
        
        return null;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#execute(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public List<IProcessingUnitRunnable> execute(List<ProcessingUnitReference> processingUnitExecutionList, IProcessingUnitContext inputProcessingUnitContext) {
        IProcessingUnitContext processingUnitContext = inputProcessingUnitContext;
        if (processingUnitContext == null) {
            processingUnitContext = new ProcessingUnitContext();
        }
        
        final List<IProcessingUnitRunnable> resultList = new ArrayList<IProcessingUnitRunnable>();
        for (ProcessingUnitReference p : processingUnitExecutionList) {
            resultList.add(execute(new ProcessingUnitRunnable(p.getId(), p.getName(), p.getProcessingUnitClass(), p.getParameterList(), processingUnitContext, this)));
        }
        
        return resultList;
    }
    
    
    /**
     * Execute a processing unit runnable
     *
     * @param processingUnitRunnable the processing unit runnable
     * @return the processing unit runnable
     */
    protected ProcessingUnitRunnable execute(final ProcessingUnitRunnable processingUnitRunnable) {
        // start execution
        threadPoolExecutor.execute(processingUnitRunnable);
        
        // add to processing unit runnable map
        processingUnitRunnableMap.put(processingUnitRunnable.getId(), processingUnitRunnable);
        return processingUnitRunnable;
    }


    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#execute(com.github.toolarium.processing.engine.impl.executer.dto.ProcessingExecuterPersistenceContainer)
     */
    @Override
    public List<IProcessingUnitRunnable> execute(ProcessingExecuterPersistenceContainer processingExecuterPersistenceContainer) {
        List<IProcessingUnitRunnable> resultList = new ArrayList<IProcessingUnitRunnable>();
        if (processingExecuterPersistenceContainer != null && processingExecuterPersistenceContainer.getSuspendedStateList() != null) {
            for (byte[] suspendedState : processingExecuterPersistenceContainer.getSuspendedStateList()) {
                ProcessingUnitRunnable p = new ProcessingUnitRunnable(suspendedState, this);
                resultList.add(p);
                execute(p);
            }
        }
        
        return resultList;
    }

    
    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#shutdown()
     */
    @Override
    public ProcessingExecuterPersistenceContainer shutdown() {
        processingExecuterPersistenceContainer = new ProcessingExecuterPersistenceContainer();
        
        for (String id : processingUnitRunnableMap.keySet()) {
            suspendProcessing(id);
        }
        
        while (!processingUnitRunnableMap.isEmpty()) {
            for (ProcessingUnitRunnable p : processingUnitRunnableMap.values()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(ProcessingUnitUtil.getInstance().toString(p.getId(), p.getName(), (String)null /*p.getprocessingUnitClass*/) + " wait on is going to: " + p.getProcessingActionStatus());
                }
            }
            
            ThreadUtil.getInstance().sleep(1000L);
        }
        
        return processingExecuterPersistenceContainer;
    }
    
    
    /**
     * @see com.github.toolarium.processing.engine.impl.executer.IProcessingExecuter#getStatus()
     */
    @Override
    public IProcessingExecuterStatus getStatus() {
        ProcessingExecuterStatus p = new ProcessingExecuterStatus();
        p.setCorePoolSize(corePoolSize);
        p.setMaxPoolSize(maximumPoolSize);
        p.setKeepAliveTimeInSeconds(keepAliveTimeInSeconds);
        p.setNumberOfRunningProcessings(processingUnitRunnableMap.size());
        return p;
    }

    
    /**
     * Set a process unit throttling
     *
     * @param id the id
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of processing units per second
     * @return this instance
     */
    public ProcessingExecuterImpl setProcessingUnitThrottling(String id, Long maxNumberOfProcessingUnitCallsPerSecond) {
        ProcessingUnitRunnable p = processingUnitRunnableMap.get(id);
        if (p != null) {
            p.setProcessingUnitThrottling(maxNumberOfProcessingUnitCallsPerSecond);
        }
        
        return this;
    }

    
    /**
     * Set the processing listener
     *
     * @param processingListener the processing listener 
     * @return this instance
     */
    public ProcessingExecuterImpl setProcessingExecuterListener(IProcessingListener processingListener) {
        this.processingListener = processingListener;
        return this;
    }
    

    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener#notifyProcessingUnitState(java.lang.String, java.lang.String, java.lang.String, 
     *      com.github.toolarium.processing.unit.dto.ProcessingActionStatus, com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement, 
     *      com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void notifyProcessingUnitState(String id, 
                                          String name, 
                                          String processingUnitClass, 
                                          ProcessingActionStatus processingActionStatus,
                                          IProcessingUnitProgress processingProgress,
                                          IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment,
                                          IProcessingUnitContext processingUnitContext) {
    
        if (processingListener != null) {
            processingListener.notifyProcessingUnitState(id, name, processingUnitClass, processingActionStatus, processingProgress, runtimeTimeMeasurment, processingUnitContext);
        }
        
        if (ProcessingActionStatus.ABORTED.equals(processingActionStatus) || ProcessingActionStatus.ENDED.equals(processingActionStatus)) {
            IProcessingUnitRunnable processingUnitRunnable = processingUnitRunnableMap.remove(id);
            if (processingUnitRunnable == null) {
                return;
            }
            
            if (processingListener != null) {
                // collect all information together
                ProcessingResult result = new ProcessingResult();
                result.setId(processingUnitRunnable.getId());
                result.setName(processingUnitRunnable.getName());
                result.setStartTimestamp(processingUnitRunnable.getTimeMeasurement().getStartTimestamp());
                result.setStopTimestamp(processingUnitRunnable.getTimeMeasurement().getStopTimestamp());
                result.setProcessingDuration(processingUnitRunnable.getTimeMeasurement().getDuration());
                result.setNumberOfProcessedUnits(processingProgress.getNumberOfProcessedUnits());
                result.setNumberOfSuccessfulUnits(processingProgress.getNumberOfSuccessfulUnits());
                result.setNumberOfFailedUnits(processingProgress.getNumberOfFailedUnits());
                result.setProcessingRuntimeStatus(processingUnitRunnable.getProcessingRuntimeStatus());
                result.setIsAborted(ProcessingActionStatus.ABORTED.equals(processingActionStatus));
                result.setStatusMessageList(processingUnitRunnable.getStatusMessageList());
                result.setProcessingStatistic(new IProcessingStatistic() {
                 
                    /**
                     * @see com.github.toolarium.processing.engine.dto.result.IProcessingStatistic#keySet()
                     */
                    @Override
                    public Set<String> keySet() {
                        return processingProgress.getProcesingUnitStatistic().keySet();
                    }


                    /**
                     * @see com.github.toolarium.processing.engine.dto.result.IProcessingStatistic#isEmpty()
                     */
                    @Override
                    public boolean isEmpty() {
                        return processingProgress.getProcesingUnitStatistic().isEmpty();
                    }
                    
                    
                    /**
                     * @see com.github.toolarium.processing.engine.dto.result.IProcessingStatistic#hasKey(java.lang.String)
                     */
                    @Override
                    public boolean hasKey(String key) {
                        return processingProgress.getProcesingUnitStatistic().hasKey(key);
                    }

                    
                    /**
                     * @see com.github.toolarium.processing.engine.dto.result.IProcessingStatistic#get(java.lang.String)
                     */
                    @Override
                    public StatisticCounter get(String key) {
                        return processingProgress.getProcesingUnitStatistic().get(key);
                    }
                });
                processingListener.notifyProcessEnd(result);
            }
        } else if (ProcessingActionStatus.SUSPENDED.equals(processingActionStatus)) {
            ProcessingUnitRunnable p = processingUnitRunnableMap.get(id);
            if (p != null) {
                final byte[] suspendedState = p.getSuspendedState();
                if (suspendedState != null && suspendedState.length > 0) {
                    processingExecuterPersistenceContainer.add(suspendedState);
                }
                
                processingUnitRunnableMap.remove(id);
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass) + " is going to: " + processingActionStatus);
            }
        }
    }


    /**
     * Suspend a processing
     *
     * @param id the id
     */
    protected void suspendProcessing(String id) {
        ProcessingUnitRunnable p = processingUnitRunnableMap.get(id);
        if (p != null) {
            p.suspendProcessing();
        }
    }


    /**
     * Get the suspended state
     *
     * @param id the id
     * @return the suspended state
     */
    protected byte[] getSuspendedState(String id) {
        ProcessingUnitRunnable p = processingUnitRunnableMap.get(id);
        if (p != null) {
            return p.getSuspendedState();
        }
        
        return null;
    }
}
