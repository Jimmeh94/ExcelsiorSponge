package com.excelsiormc.excelsiorsponge.events;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.world.ChangeWorldWeatherEvent;
import org.spongepowered.api.world.weather.Weathers;

public class WorldEvents {

    @Listener
    public void onDecay(ChangeBlockEvent.Decay event){
        event.setCancelled(true);
    }

    @Listener
    public void onRain(ChangeWorldWeatherEvent event){
        if(event.getOriginalWeather() != Weathers.CLEAR){
            event.setWeather(Weathers.CLEAR);
            event.setDuration(Integer.MAX_VALUE);
        }
    }

}
