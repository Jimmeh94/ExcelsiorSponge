package com.excelsiormc.excelsiorsponge.timers;

import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.game.user.scoreboard.ArenaDefaultPreset;
import com.excelsiormc.excelsiorsponge.game.user.scoreboard.HandViewingPreset;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandViewingTimer extends AbstractTimer {

    private List<UserPlayer> viewers;

    public HandViewingTimer(long interval) {
        super(interval);

        viewers = new CopyOnWriteArrayList<>();
    }

    public void addViewer(UserPlayer userPlayer){
        if(!viewers.contains(userPlayer)){
            viewers.add(userPlayer);
            userPlayer.changeScoreboardPreset(new HandViewingPreset(userPlayer));
        }
    }

    public void remove(UserPlayer userPlayer){
        viewers.remove(userPlayer);
        userPlayer.changeScoreboardPreset(new ArenaDefaultPreset(userPlayer, DuelUtils.getArena(userPlayer.getOwner()).get()));
    }

    @Override
    protected void runTask() {
        for(UserPlayer userPlayer: viewers){
            if(DuelUtils.getCombatProfilePlayer(userPlayer.getOwner()).get().getHand().getHeldCard().isPresent()){
                userPlayer.updateScoreboard();
            }
        }
    }
}
