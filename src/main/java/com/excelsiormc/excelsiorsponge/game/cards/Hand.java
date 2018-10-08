package com.excelsiormc.excelsiorsponge.game.cards;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.property.SlotIndex;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Hand {

    private List<CardBase> cards;
    private UUID owner;

    public Hand(UUID owner) {
        this.owner = owner;
        cards = new CopyOnWriteArrayList<>();
    }

    public Optional<CardBase> getHeldCard(){
        Player player = PlayerUtils.getPlayer(owner).get();
        Optional<SlotIndex> slot = InventoryUtils.getHeldItemSlot(player, HandTypes.MAIN_HAND);
        if(slot.isPresent()){
            if(cards.size() - 1 >= slot.get().getValue()){
                return Optional.of(cards.get(slot.get().getValue()));
            }
        } else {
            slot = InventoryUtils.getHeldItemSlot(player, HandTypes.OFF_HAND);
            if(slot.isPresent()){
                if(cards.size() - 1 >= slot.get().getValue()){
                    return Optional.of(cards.get(slot.get().getValue()));
                }
            }
        }
        return Optional.empty();
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
