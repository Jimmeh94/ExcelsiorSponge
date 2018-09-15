package com.excelsiormc.excelsiorsponge.game.match.profiles;

import com.excelsiormc.excelsiorsponge.game.cards.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;

import java.util.UUID;

public class CombatantProfilePlayer extends CombatantProfile {

    private Cell currentAim;
    private CardBase currentlyMovingCard;

    public CombatantProfilePlayer(UUID owner, Deck deck) {
        super(owner, deck);
    }

    public Cell getCurrentAim() {
        return currentAim;
    }

    public void setCurrentAim(Cell currentAim) {
        this.currentAim = currentAim;
    }

    public CardBase getCurrentlyMovingCard() {
        return currentlyMovingCard;
    }

    public void setCurrentlyMovingCard(CardBase currentlyMovingCard) {
        this.currentlyMovingCard = currentlyMovingCard;
    }

}
