package com.excelsiormc.excelsiorsponge.game.match.profiles;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseCombatant;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementSquare;
import com.excelsiormc.excelsiorsponge.game.match.cards.Hand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public abstract class CombatantProfile {

    private UUID owner;
    private Hand hand;
    private Deck deck;
    private CardBaseCombatant card;

    public CombatantProfile(UUID owner, Deck deck) {
        this.owner = owner;
        this.deck = deck.clone();

        hand = new Hand(owner);
        card = new CardBaseCombatant(owner, this, 0, new CardMovementSquare(1));
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

    public Player getPlayer(){return isPlayer() ? Sponge.getServer().getPlayer(owner).get() : null;}

    public UUID getUUID() {
        return owner;
    }
}
