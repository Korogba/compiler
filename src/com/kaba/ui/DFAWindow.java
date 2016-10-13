package com.kaba.ui;

/**
 * Created by Yusuf on 10/11/2016
 */
public class DFAWindow extends GraphWindow {
    public DFAWindow(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    protected String returnName() {
        return "Deterministic Finite Automata";
    }
}
