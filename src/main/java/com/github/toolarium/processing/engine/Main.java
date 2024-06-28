/*
 * Main.java
 *
 * Copyright by toolarium, all rights reserved.
 */

package com.github.toolarium.processing.engine;


/**
 * Main.
 *
 * <p>! This is just a sample please remove it. !</p>
 */
public final class Main {

    /**
     * Constructor for Main
     *
     */
    private Main() {
    }
    
    
    /**
     * The main class
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        IProcessEngine processEngine = ProcessingEngineFactory.getInstance().getProcessingEngine();
    }
}
