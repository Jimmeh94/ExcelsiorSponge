package com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatPlayerProfile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is a chat channel that is purely for listening to other players
 * You can specify a player to listen to and their messages will get sent here as well
 */
public class ChatChannelReceiver {

    private List<UUID> players;
    private ChatPlayerProfile owner;

    public ChatChannelReceiver(ChatPlayerProfile owner){
        this.owner = owner;
        players = new CopyOnWriteArrayList<>();
    }

    public void addPlayer(UUID uuid){
        if(!players.contains(uuid)){
            players.add(uuid);
        }
    }

    public void removePlayer(UUID uuid){
        players.remove(uuid);
    }

    public boolean has(UUID uuid){
        return players.contains(uuid);
    }

}
