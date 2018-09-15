package com.excelsiormc.excelsiorsponge.game.economy.currencies;

import com.excelsiormc.excelsiorcore.services.economy.Currency;
import com.excelsiormc.excelsiorcore.services.text.AltCodes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class CurrencyGold extends Currency {

    public CurrencyGold() {
        super(Text.of(TextColors.GOLD, TextStyles.BOLD, AltCodes.CIRCLE_STAR.getSign()),
                Text.of(TextColors.GOLD, "Gold"));
    }
}
