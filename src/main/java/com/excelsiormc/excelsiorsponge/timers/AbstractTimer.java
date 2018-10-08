package com.excelsiormc.excelsiorsponge.timers;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public abstract class AbstractTimer implements Runnable {

    protected Task task;
    private int intervalsPassed = 0, cancelAt = -1;
    private long interval, delay;
    //private List<DelayedTask> delayedTasks;

    protected abstract void runTask();

    public AbstractTimer(long interval){
        this(interval, 0L);
    }

    public AbstractTimer(long interval, long delay){
        this(interval, delay, -1);
    }

    public AbstractTimer(long interval, long delay, int cancelAt){
        this.interval = interval;
        this.delay = delay;
        this.cancelAt = cancelAt;
        //delayedTasks = new CopyOnWriteArrayList<>();
    }

    /*public void addDelayedTask(DelayedTask task){
        delayedTasks.add(task);
    }*/

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
        if(cancelAt != -1 && intervalsPassed >= cancelAt){
            stop();
        }
        runTask();

        /*for(DelayedTask task: delayedTasks){
            if(task.tick()){
                delayedTasks.remove(task);
            }
        }*/
        intervalsPassed++;
    }

    public Task getTask() {
        return task;
    }

    /*public static abstract class DelayedTask{

        protected int delayPeriods;

        public abstract void doTask();

        public DelayedTask(int delayPeriods) {
            this.delayPeriods = delayPeriods;
        }

        protected boolean tick(){
            if(delayPeriods <= 0){
                doTask();
                return true;
            }
            delayPeriods--;
            return false;
        }
    }*/
}
