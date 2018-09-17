package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.AltCodes;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class ArenaDefaultPreset extends ScoreboardPreset {

    private Arena arena;

    public ArenaDefaultPreset(PlayerBase owner, Arena arena) {
        super(owner);

        this.arena = arena;
        updateScores();
    }

    @Override
    public void updateScores() {
        List<Text> strings = new ArrayList<>();

        strings.add(Text.of(TextColors.RED, AltCodes.CROSSED_SWORDS.getSign() + " " + TextColors.GRAY 
                + arena.getGamemode().getName() + " " + TextColors.RED + AltCodes.CROSSED_SWORDS.getSign()));
        strings.add(Text.of("==============================="));
        //strings.add(owner.getPresentArea().getDisplayName());
        strings.add(Text.of(TextColors.RED.toString()));
        strings.add(Text.of(TextColors.GREEN + "Game Time Left: " + arena.getGamemode().getTimeLeftFormatted()));

        strings.add(Text.of(TextColors.RED.toString() + TextColors.GRAY.toString()));
        strings.add(Text.of(TextColors.RED + "Turn Time Left: " + arena.getGamemode().getTimeLeftInCurrentTurnFormatted()));

        setScores(strings);
    }
}
