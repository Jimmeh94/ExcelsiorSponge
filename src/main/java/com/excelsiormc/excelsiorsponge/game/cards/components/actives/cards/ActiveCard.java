package com.excelsiormc.excelsiorsponge.game.cards.components.actives.cards;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.components.actives.Active;
import org.spongepowered.api.text.Text;

public abstract class ActiveCard implements Active {

    protected CardBase card;
    protected Text displayName;

    public ActiveCard(CardBase card, Text displayName) {
        this.card = card;
        this.displayName = displayName;
    }

    public CardBase getCard() {
        return card;
    }

    public Text getDisplayName(){return displayName;}
}
