package com.excelsiormc.excelsiorsponge.excelsiorcore.services.party;

import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Party {

    private List<UUID> members;

    public Party(Player creator) {
        this.members = new CopyOnWriteArrayList<>();
        members.add(creator.getUniqueId());
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void add(UUID uuid){
        if(!members.contains(uuid)){
            members.add(uuid);
        }
    }

    public void remove(UUID uuid){
        members.remove(uuid);
    }

    public boolean isMember(UUID uuid){
        return members.contains(uuid);
    }
}
