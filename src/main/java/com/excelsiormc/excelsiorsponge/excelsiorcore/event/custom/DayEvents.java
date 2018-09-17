package com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Calendar;
import org.spongepowered.api.event.cause.Cause;

public abstract class DayEvents extends CustomEvent {

    private Calendar.Time time;

    public DayEvents(Cause cause, Calendar.Time time) {
        super(cause);

        this.time = time;
    }

    public Calendar.Time getTime() {
        return time;
    }

    public static class DayBeginEvent extends DayEvents{

        public DayBeginEvent(Cause cause, Calendar.Time time) {
            super(cause, time);
        }
    }
}
