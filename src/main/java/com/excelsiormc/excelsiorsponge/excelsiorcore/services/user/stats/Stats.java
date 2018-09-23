package com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Stats<T> {

    private Map<String, StatBase> stats;
    private T owner;

    public Stats(T owner) {
        this.owner = owner;

        stats = new HashMap<>();
    }

    public T getOwner(){
        return owner;
    }

    public boolean isOwner(T check){
        return owner.equals(check) || owner == check;
    }

    public void addStat(String id, StatBase stat){
        stats.put(id, stat);
    }

    public Optional<StatBase> getStat(String id){
        return stats.containsKey(id) ? Optional.of(stats.get(id)) : Optional.empty();
    }

    public boolean hasStat(String id){
        return stats.containsKey(id);
    }

    public void removeStat(String id){
        stats.remove(id);
    }

    public Collection<StatBase> getStats() {
        return stats.values();
    }
}
