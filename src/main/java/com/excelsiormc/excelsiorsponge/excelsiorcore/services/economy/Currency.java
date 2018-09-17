package com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy;

import org.spongepowered.api.text.Text;

public abstract class Currency {

    private Text symbol, displayName;

    public Currency(Text symbol, Text displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    public Text getSymbol() {
        return symbol;
    }

    public Text getDisplayName() {
        return displayName;
    }
}
