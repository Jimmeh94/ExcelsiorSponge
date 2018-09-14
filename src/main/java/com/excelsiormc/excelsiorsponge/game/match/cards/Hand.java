package com.excelsiormc.excelsiorsponge.game.match.cards;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Hand {

    private List<CardBase> cards;
    private UUID owner;

    public Hand(UUID owner) {
        this.owner = owner;
        cards = new CopyOnWriteArrayList<>();
    }

    public void addCard(CardBase card){
        cards.add(card);
    }

    public void removeCard(CardBase card){
        cards.remove(card);
    }

    public boolean canDrawCard(){
        return cards.size() < 6;
    }

    public CardBase viewCard(int index){
        return cards.get(index);
    }

    public CardBase getCard(int index){
        CardBase card = cards.remove(index);
        return card;
    }

    public int getSize() {
        return cards.size();
    }

    public List<CardBase> getCards() {
        return cards;
    }

    public void removeCard(int index) {
        cards.remove(index);
    }

    public boolean hasCardAt(int index) {
        return cards.get(index) != null;
    }
}
