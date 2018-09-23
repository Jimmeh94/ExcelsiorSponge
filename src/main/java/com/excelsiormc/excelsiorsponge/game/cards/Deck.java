package com.excelsiormc.excelsiorsponge.game.cards;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {

    private List<CardBase> cards;
    private UUID uuid;

    public Deck(UUID uuid) {
        this.uuid = uuid;

        cards = new CopyOnWriteArrayList<>();
    }

    public void addCard(CardBase cardBase){
        cards.add(cardBase);
    }

    public List<CardBase> getCards() {
        return cards;
    }

    public UUID getOwner(){
        return uuid;
    }

    public void shuffleCards(){
        Collections.shuffle(cards);
    }

    public int getRemainingCardAmount(){
        return cards.size();
    }

    public void arrangeDeckByLevel(boolean ascending){
        bubbleSortLevel(ascending);
    }

    protected void bubbleSortLevel(boolean ascending) {
        int n = cards.size();
        CardBase temp;
        for(int i = 0; i < n; i++){
            for(int j = 1; j < (n-i); j++){
                if((ascending && (cards.get(j - 1).getLevel() > cards.get(j).getLevel())) ||
                        (!ascending && (cards.get(j - 1).getLevel() < cards.get(j).getLevel()))){
                    //swap elements
                    temp = cards.get(j - 1);
                    cards.set(j - 1, cards.get(j));
                    cards.set(j, temp);
                }

            }
        }

    }

    public Deck clone(){
        Deck give = new Deck(uuid);
        give.cards = new CopyOnWriteArrayList<>(cards);
        return give;
    }

    public CardBase getNextCard(boolean removeFromDeck) {
        CardBase give = cards.get(0);
        if(removeFromDeck){
            cards.remove(0);
        }
        return give;
    }
}
