package com.excelsiormc.excelsiorsponge.game.match.matchmaking;

import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemodes;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Queue {

    private List<UserPlayer> players;
    private Gamemodes gamemode;

    public Queue(Gamemodes gamemode) {
        players = new CopyOnWriteArrayList<>();
        this.gamemode = gamemode;
    }

    public Gamemodes getGamemode() {
        return gamemode;
    }

    public void addPlayer(UserPlayer userPlayer){
        if(!players.contains(userPlayer)){
            players.add(userPlayer);
        }
    }

    public void removePlayer(UserPlayer userPlayer){
        players.remove(userPlayer);
    }

    public boolean canStart() {
        return players.size() >= gamemode.getDescription().getCombatants();
    }
}
