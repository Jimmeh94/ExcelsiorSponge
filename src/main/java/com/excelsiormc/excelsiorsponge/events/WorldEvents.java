package com.excelsiormc.excelsiorsponge.events;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class WorldEvents {

    @Listener
    public void onDecay(ChangeBlockEvent.Decay event){
        event.setCancelled(true);
    }

}
