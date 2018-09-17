package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy.Account;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy.Economy;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.economy.currencies.Currencies;
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

        strings.add(Text.of(TextColors.GRAY, "Excelsior"));
        strings.add(Text.of("=============="));
        //strings.add(owner.getPresentArea().getDisplayName());
        strings.add(Text.of(TextColors.RED, ""));

        Account account = ExcelsiorCore.INSTANCE.getEconomy().getOrCreateAccount(getOwner().getOwner()).get();
        strings.add(Text.of(TextColors.GREEN, "Balance: " + account.getBalance(Currencies.GOLD)));

        setScores(strings);
    }
}
