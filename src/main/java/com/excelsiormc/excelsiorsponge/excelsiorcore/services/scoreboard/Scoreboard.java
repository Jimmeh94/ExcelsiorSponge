package com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

public class Scoreboard {

    private PlayerBase owner;
    private ScoreboardPreset preset;

    public Scoreboard(PlayerBase player, ScoreboardPreset defaultPreset){
        owner = player;

        org.spongepowered.api.scoreboard.Scoreboard scoreboard = org.spongepowered.api.scoreboard.Scoreboard.builder()
                .objectives(Arrays.asList(Objective.builder().name("Side").criterion(Criteria.DUMMY).displayName(Text.EMPTY).build())).build();

        owner.getPlayer().setScoreboard(scoreboard);
        scoreboard.updateDisplaySlot(scoreboard.getObjective("Side").get(), DisplaySlots.SIDEBAR);

        preset = defaultPreset;

        updatePreset();

        Objective objective = owner.getPlayer().getScoreboard().getObjective(DisplaySlots.SIDEBAR).get();
        objective.setDisplayName(preset.getScore(0));

        for(int i = 1; i < preset.getOldSnapshot().size(); i++){
            objective.getOrCreateScore(preset.getScore(i)).setScore(16 - i);
        }
    }

    private void updatePreset(){ //use this before updating scoreboard
        preset.takeSnapshot();
        preset.updateScores();
    }

    public void setPreset(ScoreboardPreset preset){
        if(preset.getClass() == this.preset.getClass())
            return;
        this.preset.takeSnapshot();

        Objective objective = owner.getPlayer().getScoreboard().getObjective(DisplaySlots.SIDEBAR).get();
        objective.setDisplayName(preset.getScore(0));

        for(int i = 0; i < this.preset.getOldSnapshot().size(); i++){
            objective.removeScore(this.preset.getOldSnapshot().get(i));
        }

        this.preset = preset;
        updateScoreboard();
    }

    public void unregisterScoreboard(){
        Player owner = this.owner.getPlayer();
        owner.setScoreboard(null);
    }

    public void updateScoreboard(){//sidebar scoreboard
        updatePreset();
        Objective objective = owner.getPlayer().getScoreboard().getObjective(DisplaySlots.SIDEBAR).get();
        objective.setDisplayName(preset.getScore(0));

        //starting at 1 because 0 is the title
        for(int i = 1; i < preset.getOldSnapshot().size(); i++){
            if(!preset.getScores().contains(preset.getOldSnapshot().get(i))){
                objective.removeScore(preset.getOldSnapshot().get(i));
            }
        }

        for(int i = 1; i < preset.getScores().size(); i++){
            if(!objective.hasScore(preset.getScore(i))){
                objective.getOrCreateScore(preset.getScore(i)).setScore(16 - i);
            }
        }
    }

    public ScoreboardPreset getPreset() {
        return preset;
    }
}
