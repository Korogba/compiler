package com.kaba.ui;

/**
 * Created by Yusuf on 10/11/2016
 */
public class NFAWindow extends GraphWindow {

    public NFAWindow(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    protected String returnName() {
        return "Non-Deterministic Finite Automata";
    }
}
