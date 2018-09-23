package com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats;

import org.spongepowered.api.text.Text;

public class StatBaseRegenerative extends StatBase {

    protected double regenAmount;

    public StatBaseRegenerative(double current, double max, Text displayName, double regenAmount) {
        super(current, max, displayName);

        this.regenAmount = regenAmount;
    }

    public void setRegenAmount(double regenAmount) {
        this.regenAmount = regenAmount;
    }

    public double getRegenAmount() {
        return regenAmount;
    }

    public void regenerate(){
        if(getCurrent() < getMax()){
            add(regenAmount);
        }
    }
}
