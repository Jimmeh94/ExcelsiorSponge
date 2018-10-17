package com.excelsiormc.excelsiorsponge.timers;

import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.HashMap;
import java.util.Map;

public class WorldTimer extends AbstractTimer {

    private Map<World, TimeOfDay> worlds;

    public WorldTimer() {
        super(20L);

        worlds = new HashMap<>();
    }

    public void addWorld(World world, TimeOfDay timeOfDay){
        worlds.put(world, timeOfDay);
    }

    @Override
    protected void runTask() {
        for(Map.Entry<World, TimeOfDay> entry: worlds.entrySet()){
            WorldProperties properties = entry.getKey().getWorldStorage().getWorldProperties();
            long temp = properties.getWorldTime() % 24000;
            properties.setWorldTime(temp + (entry.getValue().getTime() - temp));
            //properties.setWorldTime(((int) Math.ceil(properties.getWorldTime() / 24000)) * entry.getValue().getTime());
        }
    }

    public enum TimeOfDay{
        NOON(6000),
        MIDNIGHT(18000),
        MORNING(24000),
        EVENING(12000);

        private long time;

        TimeOfDay(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }
    }
}
