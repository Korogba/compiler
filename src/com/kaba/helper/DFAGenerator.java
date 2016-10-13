package com.kaba.helper;

import org.graphstream.algorithm.generator.BaseGenerator;

/**
 * Created by Yusuf on 10/12/2016
 */
public class DFAGenerator extends BaseGenerator {

    public DFAGenerator(boolean directed, boolean randomlyDirectedEdges) {
        super(directed, randomlyDirectedEdges);
    }

    @Override
    public void begin() {
    }

    @Override
    public boolean nextEvents() {
        return true;
    }

    @Override
    public void end() {

    }
}
