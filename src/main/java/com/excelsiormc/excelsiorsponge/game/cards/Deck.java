package com.excelsiormc.excelsiorsponge.game.cards;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetypes;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {

    protected List<CardBase> cards;
    protected UUID uuid;
    protected Archetypes primary, secondary;

    public Deck(UUID uuid, Archetypes primary, Archetypes secondary) {
        this.uuid = uuid;
        this.primary = primary;
        this.secondary = secondary;

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

    public void arrangeDeckByName(){
        bubbleSortLevel();
    }

    protected void bubbleSortLevel() {
        int n = cards.size();
        CardBase temp;
        for(int i = 0; i < n; i++){
            for(int j = 1; j < (n-i); j++){
                if(cards.get(j - 1).getName().toPlain().compareTo(cards.get(j).getName().toPlain()) > 0){
                    //swap elements
                    temp = cards.get(j - 1);
                    cards.set(j - 1, cards.get(j));
                    cards.set(j, temp);
                }

            }
        }

    }

    public Deck clone(){
        Deck give = new Deck(uuid, primary, secondary);
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

    public boolean isHinderingTerrain(CellTerrains cellType) {
        return Archetypes.getHindering(primary, secondary).contains(cellType);
    }

    public boolean isAdvantageousTerrain(CellTerrains cellType) {
        return Archetypes.getAdvantages(primary, secondary).contains(cellType);
    }
}
