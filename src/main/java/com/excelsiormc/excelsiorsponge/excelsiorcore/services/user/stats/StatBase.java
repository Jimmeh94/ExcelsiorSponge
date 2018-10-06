package com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.StatEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

public abstract class StatBase {

    private double current, max;
    private Text displayName;

    /**
     * This will allow this stat to be buffed/debuffed then restored to this point
     */
    private StatBase restorePoint;

    public StatBase(double current, double max, Text displayName) {
        this.current = current;
        this.max = max;
        this.displayName = displayName;
    }

    protected StatBase(StatBase statBase){
        this.current = new Double(statBase.current);
        this.max = new Double(statBase.max);
        this.displayName = statBase.displayName;
    }

    public double getCurrent() {
        return current;
    }

    public double getMax() {
        return max;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public void subtract(double amount){
        current -= amount;
        validate();
        Sponge.getEventManager().post(new StatEvent.StatDecreaseEvent(ExcelsiorSponge.getServerCause(), this));
    }

    public void add(double amount){
        current += amount;
        validate();
        Sponge.getEventManager().post(new StatEvent.StatIncreaseEvent(ExcelsiorSponge.getServerCause(), this));
    }

    protected void validate(){
        if(current < 0){
            current = 0;
        } else if(current > max){
            current = max;
        }
    }

    //public void generateRestorePoint(){
        //restorePoint = new StatBase(this);
    //}

    public void restore(){
        if(restorePoint != null){
            this.current = restorePoint.current;
            this.max = restorePoint.max;
            this.displayName = restorePoint.displayName;
            restorePoint = null;
            Sponge.getEventManager().post(new StatEvent.StatRestoreEvent(ExcelsiorSponge.getServerCause(), this));
        }
    }
}
