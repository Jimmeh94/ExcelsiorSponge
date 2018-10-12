package com.excelsiormc.excelsiorsponge.game.cards.decks;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetypes;
import com.excelsiormc.excelsiorsponge.game.cards.cards.CardDummy;
import com.excelsiormc.excelsiorsponge.game.cards.cards.CardDummyThree;
import com.excelsiormc.excelsiorsponge.game.cards.cards.CardDummyTwo;

import java.util.UUID;

public class DeckDummy extends Deck {

    public DeckDummy(UUID uuid) {
        super(uuid, Archetypes.PISCES, Archetypes.CAPRICORN);

        for(int i = 0; i < 20; i++){
            addCard(new CardDummy(getOwner()));
        }
        for(int i = 0; i < 20; i++){
            addCard(new CardDummyTwo(getOwner()));
        }
        for(int i = 0; i < 10; i++){
            addCard(new CardDummyThree(getOwner()));
        }
    }
}
