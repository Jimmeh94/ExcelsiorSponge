package com.excelsiormc.excelsiorsponge.game.cards.stats;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StatPower extends StatBase {

    public StatPower(double amount){
        this(amount, amount);
    }

    public StatPower(double current, double max) {
        super(current, max, Text.of(TextColors.YELLOW, "Power: "));
    }
}
