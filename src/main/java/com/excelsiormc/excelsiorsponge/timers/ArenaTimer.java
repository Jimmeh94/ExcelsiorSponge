package com.excelsiormc.excelsiorsponge.timers;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;

public class ArenaTimer extends AbstractTimer {

    public ArenaTimer(long interval) {
        super(interval);
    }

    @Override
    protected void runTask() {
        ExcelsiorSponge.INSTANCE.getArenaManager().tick();
    }
}
