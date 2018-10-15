package com.excelsiormc.excelsiorsponge.game.cards.components.passives.card;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.components.passives.Passive;

public abstract class PassiveCard implements Passive {

    private CardBase card;

    public PassiveCard(CardBase card) {
        this.card = card;
    }

    public CardBase getCard() {
        return card;
    }
}
