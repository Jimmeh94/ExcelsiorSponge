package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class DefaultPreset extends ScoreboardPreset {

    public DefaultPreset(PlayerBase player){
        super(player);
        updateScores();
    }

    @Override
    public void updateScores() {
        List<Text> strings = new ArrayList<>();

        strings.add(Text.of(TextColors.GRAY + "Excelsior"));
        strings.add(Text.of("=============="));
        //strings.add(owner.getPresentArea().getDisplayName());
        strings.add(Text.of(TextColors.RED.toString()));
        strings.add(Text.of(TextColors.GREEN + "Balance: " + ExcelsiorCore.INSTANCE.getEconomy().getOrCreateAccount(getOwner().getOwner()).get().getBalance(null)));

        setScores(strings);
    }
}
