package com.excelsiormc.excelsiorsponge.game.cards.cards.decks;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptors;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetypes;
import com.excelsiormc.excelsiorsponge.managers.CardManager;

import java.util.Random;
import java.util.UUID;

public class DeckStarter extends Deck {

    public DeckStarter(UUID uuid) {
        super(uuid, Archetypes.PISCES, Archetypes.CAPRICORN);

        for(CardDescriptors c: CardDescriptors.values()){
            addCard(c);
        }

        for(int i = 0; i < 50 - cards.size(); i++){
            addCard(CardDescriptors.values()[(new Random()).nextInt(CardDescriptors.values().length)]);
        }
    }

    public void addCard(CardDescriptors descriptor){
        addCard(CardManager.getCardInstance(descriptor, getOwner()));
    }
}
