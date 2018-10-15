package com.excelsiormc.excelsiorsponge.game.cards.components.actives.cards;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.components.actives.Active;

public abstract class ActiveCard implements Active {

    private CardBase card;

    public ActiveCard(CardBase card) {
        this.card = card;
    }

    public CardBase getCard() {
        return card;
    }
}
