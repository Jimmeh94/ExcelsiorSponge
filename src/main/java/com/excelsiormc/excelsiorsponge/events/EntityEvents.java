package com.excelsiormc.excelsiorsponge.events;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

public class EntityEvents {

    @Listener
    public void onSpawn(SpawnEntityEvent event){
        for(Entity e: event.getEntities()){
            if(!shouldSpawn(e.getType())){
                event.setCancelled(true);
                return;
            }
        }

    }

    private boolean shouldSpawn(EntityType type){
        return type == EntityTypes.HUMAN || type == EntityTypes.ARMOR_STAND;
    }

}
