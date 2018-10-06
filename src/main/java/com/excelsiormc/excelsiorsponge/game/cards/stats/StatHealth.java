package com.excelsiormc.excelsiorsponge.game.cards.stats;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StatHealth extends StatBase {

    public StatHealth(double current, double max) {
        super(current, max, Text.of(TextColors.RED, "Health: "));
    }
}
