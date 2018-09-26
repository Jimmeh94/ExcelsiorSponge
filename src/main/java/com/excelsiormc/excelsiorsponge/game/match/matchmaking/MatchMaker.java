package com.excelsiormc.excelsiorsponge.game.match.matchmaking;

import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.GamemodeDuel;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.managers.ManagerArena;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

public class MatchMaker {

    private ManagerArena arenaManager;

    public MatchMaker() {
        this.arenaManager = new ManagerArena();
    }

    public ManagerArena getArenaManager() {
        return arenaManager;
    }

    public void playerLeave(Player player) {
        arenaManager.checkPlayer(player);

        playerLeaveQueue(player);
    }

    public void playerLeaveQueue(Player player){
        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player.getUniqueId()).get();
        for(Queues queues: Queues.values()){
            queues.getQueue().removePlayer(userPlayer);
        }
    }

    public void playerJoinQueue(Player player, Queues queue){
        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player.getUniqueId()).get();
        queue.getQueue().addPlayer(userPlayer);

        if(queue.getQueue().canStart()){
            startGame(queue.getQueue());
        }
    }

    private void startGame(Queue queue){
        /**
         * Find arena
         * create teams
         * create gamemode
         * arena.get.start
         */
        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player.getUniqueId()).get();
        Gamemode gamemode = new GamemodeDuel(arena.get().getWorld());
        gamemode.addTeam(new Team(new CombatantProfilePlayer(player.getUniqueId(), userPlayer.getDeck())));
        arena.get().start(gamemode);
    }
}
