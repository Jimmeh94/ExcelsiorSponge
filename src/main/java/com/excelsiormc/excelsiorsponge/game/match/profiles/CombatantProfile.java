package com.excelsiormc.excelsiorsponge.game.match.profiles;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.match.cards.Hand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public abstract class CombatantProfile {

    private UUID owner;
    private Hand hand;
    private Deck deck;

    private double health, energy;

    public CombatantProfile(UUID owner, Deck deck) {
        this.owner = owner;
        this.deck = deck.clone();

        health = 800;
        energy = 100;

        hand = new Hand(owner);
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

    public Deck getDeck() {
        return deck;
    }

    public double getHealth() {
        return health;
    }

    public double getEnergy() {
        return energy;
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
