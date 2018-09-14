package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import ecore.services.messages.AltCodes;
import ecore.services.scoreboard.presets.ScoreboardPreset;
import excelsior.game.match.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaDefaultPreset extends ScoreboardPreset {

    private Arena arena;

    public ArenaDefaultPreset(Player owner, Arena arena) {
        super(owner);

        this.arena = arena;
        updateScores();
    }

    @Override
    public void updateScores() {
        List<String> strings = new ArrayList<>();

        strings.add(ChatColor.RED + AltCodes.CROSSED_SWORDS.getSign() + " " + ChatColor.GRAY + arena.getGamemode().getName() + " " + ChatColor.RED + AltCodes.CROSSED_SWORDS.getSign());
        strings.add("===============================");
        //strings.add(owner.getPresentArea().getDisplayName());
        strings.add(ChatColor.RED.toString());
        strings.add(ChatColor.GREEN + "Game Time Left: " + arena.getGamemode().getTimeLeftFormatted());

        strings.add(ChatColor.RED.toString() + ChatColor.GRAY.toString());
        strings.add(ChatColor.RED + "Turn Time Left: " + arena.getGamemode().getTimeLeftInCurrentTurnFormatted());

        setScores(strings);
    }
}
