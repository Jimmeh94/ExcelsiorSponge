package com.excelsiormc.excelsiorsponge.timers;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public abstract class AbstractTimer implements Runnable {

    protected Task task;
    private int intervalsPassed = 0, cancelAt;
    private long interval, delay;

    protected abstract void runTask();

    public AbstractTimer(long interval){
        this(interval, 0L);
    }

    public AbstractTimer(long interval, long delay){
        this(interval, delay, 0);
    }

    public AbstractTimer(long interval, long delay, int cancelAt){
        this.interval = interval;
        this.delay = delay;
        this.cancelAt = cancelAt;
    }

    public void start(){
        Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
        task = taskBuilder.delayTicks(delay).intervalTicks(interval).execute(this).submit(ExcelsiorSponge.INSTANCE);
    }

    public void stop(){
        if (task == null)
            return;
        try {
            task.cancel();
            task = null;
        } catch (IllegalStateException exc) {
        }
    }

    @Override
    public void run() {
        if(cancelAt != 0 && intervalsPassed >= cancelAt){
            stop();
            return;
        }
        runTask();
        intervalsPassed++;
    }

    public Task getTask() {
        return task;
    }
}
