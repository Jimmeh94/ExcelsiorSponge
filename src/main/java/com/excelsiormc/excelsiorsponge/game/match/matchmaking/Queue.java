package com.excelsiormc.excelsiorsponge.game.match.matchmaking;

import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemodes;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
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

    public void assignTeams(Team... teams) {
        if(gamemode == Gamemodes.DUEL){
            Team one = teams[0];
            Team two = teams[1];

            one.addCombatant(new CombatantProfilePlayer(players.get(0).getPlayer().getUniqueId(), players.get(0).getDeck()));

            if(players.size() == 1){
                two.addCombatant(new CombatantProfilePlayer(players.get(0).getPlayer().getUniqueId(), players.get(0).getDeck()));
            } else {
                two.addCombatant(new CombatantProfilePlayer(players.get(1).getPlayer().getUniqueId(), players.get(1).getDeck()));
            }
        }
    }

    public void clear() {
        players.clear();
    }
}
