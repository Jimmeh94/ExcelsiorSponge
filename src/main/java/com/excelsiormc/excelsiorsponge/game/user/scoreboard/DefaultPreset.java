package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import ecore.ECore;
import ecore.services.scoreboard.presets.ScoreboardPreset;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultPreset extends ScoreboardPreset {

    public DefaultPreset(Player player){
        super(player);
        updateScores();
    }

    @Override
    public void updateScores() {
        Player owner = getOwner();
        List<String> strings = new ArrayList<>();

        strings.add(ChatColor.GRAY + "Excelsior");
        strings.add("==============");
        //strings.add(owner.getPresentArea().getDisplayName());
        strings.add(ChatColor.RED.toString());
        strings.add(ChatColor.GREEN + "Balance: " + ECore.INSTANCE.getEconomy().findAccount(owner.getUniqueId()).get().getBalance());

        setScores(strings);
    }
}
