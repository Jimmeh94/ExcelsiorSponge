package com.excelsiormc.excelsiorsponge.game.cards.stats;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StatSummonEnergy extends StatBase {

    public StatSummonEnergy(double current, double max) {
        super(current, max, Text.of(TextColors.AQUA, "Energy: "));
    }
}
