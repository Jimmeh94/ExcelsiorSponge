package com.excelsiormc.excelsiorsponge.game.match.profiles;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;

import java.util.Optional;
import java.util.UUID;

public class CombatantProfilePlayer extends CombatantProfile {

    private Optional<Cell> currentAim = Optional.empty();
    private CardBase currentlyMovingCard;

    public CombatantProfilePlayer(UUID owner, Deck deck) {
        super(owner, deck);
    }

    public Optional<Cell> getCurrentAim() {
        return currentAim;
    }

    public void setCurrentAim(Cell currentAim) {
        if(currentAim == null){
            this.currentAim = Optional.empty();
        } else {
            this.currentAim = Optional.of(currentAim);
        }
    }

    public CardBase getCurrentlyMovingCard() {
        return currentlyMovingCard;
    }

    public void startMovingCard(CardBase card){
        this.currentlyMovingCard = card;
        currentlyMovingCard.displayAvailableSpotsToMoveTo();
        getUserPlayer().setPlayerMode(UserPlayer.PlayerMode.ARENA_MOVING_CARD);
    }

    public void stopMovingCard(){
        if(currentlyMovingCard != null){
            currentlyMovingCard.clearCurrentlyHighlighted();
        }
        this.currentlyMovingCard = null;
        getUserPlayer().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
    }

    public UserPlayer getUserPlayer(){return PlayerUtils.getUserPlayer(getUUID()).get();}

}
