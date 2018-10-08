package com.excelsiormc.excelsiorsponge.timers;

public abstract class DelayedOneUseTimer extends AbstractTimer {

    public DelayedOneUseTimer(long delay) {
        super(20L, delay, 1);

        start();
    }
}
