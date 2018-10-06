package com.excelsiormc.excelsiorsponge.game.match.profiles;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.DeckGraveyard;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseCombatant;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementSquare;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.Hand;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public abstract class CombatantProfile {

    private UUID owner;
    private Hand hand;
    private Deck deck;
    private DeckGraveyard graveyard;
    private CardBaseCombatant card;
    protected int summonEnergy;

    public CombatantProfile(UUID owner, Deck deck) {
        this.owner = owner;
        this.deck = deck.clone();
        graveyard = new DeckGraveyard(owner);

        hand = new Hand(owner);
        card = new CardBaseCombatant(owner, this, 0, new StatHealth(1000, 1000), new CardMovementSquare(1, new FilterIncludeEmptyCell()));

        summonEnergy = 1;
    }

    public void increaseSummonEnergy(int amount){
        summonEnergy += amount;
    }

    public void decreaseSummonEnergy(int amount){
        summonEnergy -= amount;
    }

    public int getSummonEnergy() {
        return summonEnergy;
    }

    public void addToGraveyard(CardBase cardBase){
        graveyard.addCard(cardBase);
    }

    public DeckGraveyard getGraveyard() {
        return graveyard;
    }

    public void drawHand(){
        //draw 4 cards
        for(int i = 0; i < 4; i++){
            drawCard();
        }
    }

    public boolean drawCard(){
        if(hand.canDrawCard()) {
            hand.addCard(deck.getNextCard(true));
            return true;
        }
        return false;
    }

    public CardBaseCombatant getCard() {
        return card;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isPlayer(){
        return this instanceof CombatantProfilePlayer;
    }

    public Player getPlayer(){return isPlayer() ? PlayerUtils.getPlayer(owner).get() : null;}

    public UUID getUUID() {
        return owner;
    }
}
