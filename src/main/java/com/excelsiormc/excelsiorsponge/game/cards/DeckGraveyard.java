package com.excelsiormc.excelsiorsponge.game.cards;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;

import java.util.UUID;

public class DeckGraveyard extends Deck {

    public DeckGraveyard(UUID uuid) {
        super(uuid, null, null);
    }

    @Override
    public void addCard(CardBase cardBase){
        if(cards.size() == 0){
            cards.add(cardBase);
        } else {
            cards.set(0, cardBase);
        }
    }
}
