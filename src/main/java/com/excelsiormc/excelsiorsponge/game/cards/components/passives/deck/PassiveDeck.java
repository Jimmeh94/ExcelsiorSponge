package com.excelsiormc.excelsiorsponge.game.cards.components.passives.deck;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.components.passives.Passive;

/**
 * These would be passives inherited from deck element and/or zodiac sign
 */
public abstract class PassiveDeck implements Passive {

    private Deck deck;

    public PassiveDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }
}
