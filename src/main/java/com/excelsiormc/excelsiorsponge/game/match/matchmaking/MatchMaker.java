package com.excelsiormc.excelsiorsponge.game.match.matchmaking;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.GamemodeDuel;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemodes;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.managers.ManagerArena;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

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
        Optional<UserPlayer> op = PlayerUtils.getUserPlayer(player.getUniqueId());
        if(op.isPresent()){
            UserPlayer userPlayer = op.get();
            for(Queues queues: Queues.values()){
                queues.getQueue().removePlayer(userPlayer);
                Messager.sendMessage(userPlayer.getPlayer(), queues.getLeaveMessage(), Messager.Prefix.ERROR);
            }
        }

    }

    public void playerJoinQueue(Player player, Queues queue){
        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player.getUniqueId()).get();

        if(queue.getQueue().hasPlayer(userPlayer)){
            Messager.sendMessage(player, Text.of(TextColors.RED, "You're already in the queue!"), Messager.Prefix.ERROR);
            return;
        }

        queue.getQueue().addPlayer(userPlayer);

        Messager.sendMessage(player, queue.getJoinMessage(), Messager.Prefix.SUCCESS);

        if(queue.getQueue().canStart()){
            startGame(queue.getQueue());
            Messager.sendMessage(player, Text.of(TextColors.GREEN, "Match found!"), Messager.Prefix.SUCCESS);
        }
    }

    private void startGame(Queue queue){
        /**
         * Find arena
         * create teams
         * create gamemode
         * arena.get.start
         */

        Arena arena = arenaManager.getAvailableArena().get();
        Gamemode gamemode = null;

        if(queue.getGamemode() == Gamemodes.DUEL){

            Team one = new Team(), two = new Team();
            queue.assignTeams(one, two);

            gamemode = new GamemodeDuel(arena.getWorld());
            gamemode.addTeam(one);
            gamemode.addTeam(two);
        }

        arena.start(gamemode);

        queue.clear();
    }
}
