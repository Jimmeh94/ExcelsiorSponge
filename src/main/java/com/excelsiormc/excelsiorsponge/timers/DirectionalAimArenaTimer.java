package com.excelsiormc.excelsiorsponge.timers;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;

public class DirectionalAimArenaTimer extends AbstractTimer {

    public DirectionalAimArenaTimer(long interval) {
        super(interval);
    }

    @Override
    protected void runTask() {
        ExcelsiorSponge.INSTANCE.getArenaManager().updatePlayersAim();
    }
}
