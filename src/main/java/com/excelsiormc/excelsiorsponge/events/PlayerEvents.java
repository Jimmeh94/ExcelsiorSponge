package com.excelsiormc.excelsiorsponge.events;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerEvents {

    @Listener
    public void onJoin(ClientConnectionEvent.Join event){
        event.setMessageCancelled(true);
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event){
        event.setMessageCancelled(true);
    }

}
