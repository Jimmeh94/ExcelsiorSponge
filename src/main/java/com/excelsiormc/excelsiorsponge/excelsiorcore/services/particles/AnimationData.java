package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public class AnimationData {

    private Task task;
    private long delay, interval, cancel; //cancel should be the amount of intervals to run

    public AnimationData(long cancel, long interval, long delay) {
        this.cancel = cancel;
        this.interval = interval;
        this.delay = delay;
    }

    public Task.Builder getTaskBuilder(){
        return Sponge.getScheduler().createTaskBuilder();
    }

    public Task getTask() {
        return task;
    }

    public long getDelay() {
        return delay;
    }

    public long getInterval() {
        return interval;
    }

    public long getCancel() {
        return cancel;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
