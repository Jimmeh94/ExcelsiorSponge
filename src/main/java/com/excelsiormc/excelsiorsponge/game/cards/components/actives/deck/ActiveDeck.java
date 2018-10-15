package com.excelsiormc.excelsiorsponge.game.cards.components.actives.deck;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.components.actives.Active;

/**
 * This is an ability that can be manually activated and is assigned by
 * the deck element and/or zodiac sign
 */
public abstract class ActiveDeck implements Active {

    private Deck deck;

    public ActiveDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }
}
