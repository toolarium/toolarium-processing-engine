/*
 * IProcessStatistic.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.engine.dto.result;

import com.github.toolarium.common.statistic.StatisticCounter;
import java.util.Set;


/**
 * The processing statistic
 * 
 * @author patrick
 */
public interface IProcessingStatistic {
    
    /**
     * Returns the statistic key set
     *
     * @return the key set
     */
    Set<String> keySet();

    
    /**
     * Test if a key is defined or not
     *
     * @param key the key
     * @return true if it exist
     */
    boolean hasKey(String key);


    /**
     * Gets the statistic counter
     *
     * @param key the statistic key
     * @return the value if it was set; otherwise null
     */
    StatisticCounter get(String key);
    
    
    /**
     * Check if the statistic if empty
     *
     * @return true if it is empty
     */
    boolean isEmpty();
}
