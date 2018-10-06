package com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import org.spongepowered.api.event.cause.Cause;

public abstract class StatEvent extends CustomEvent {

    private StatBase stat;

    public StatEvent(Cause cause, StatBase stat) {
        super(cause);

        this.stat = stat;
    }

    public StatBase getStat() {
        return stat;
    }

    public static class StatIncreaseEvent extends StatEvent {

        public StatIncreaseEvent(Cause cause, StatBase stat) {
            super(cause, stat);
        }
    }

    public static class StatDecreaseEvent extends StatEvent {

        public StatDecreaseEvent(Cause cause, StatBase stat) {
            super(cause, stat);
        }
    }

    public static class StatRestoreEvent extends StatEvent {

        public StatRestoreEvent(Cause cause, StatBase stat) {
            super(cause, stat);
        }
    }
}
