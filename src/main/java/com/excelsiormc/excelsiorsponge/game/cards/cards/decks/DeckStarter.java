package com.excelsiormc.excelsiorsponge.game.cards.cards.decks;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetypes;
import com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox.CardAncientTreeofEnlightenment;

import java.util.UUID;

public class DeckStarter extends Deck {

    public DeckStarter(UUID uuid) {
        super(uuid, Archetypes.PISCES, Archetypes.CAPRICORN);

        addCard(new CardAncientTreeofEnlightenment(uuid));
    }
}
