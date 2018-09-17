package com.excelsiormc.excelsiorsponge.excelsiorcore.services;

import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class InventoryUtils {

    public static Optional<SlotPos> getSlotPos(final ItemStack itemStack, final Player player) {
        final Slot slot = player.getInventory().<Slot>query(QueryOperationTypes.ITEM_STACK_EXACT.of(itemStack));
        if (!slot.hasChildren()) {
            return Optional.of((new ArrayList<>(slot.getProperties(SlotPos.class)).get(0)));
        }

        return Optional.empty();
    }

    public static Optional<SlotIndex> getSlotIndex(final ItemStack itemStack, final Player player) {
        final Slot slot = player.getInventory().<Slot>query(QueryOperationTypes.ITEM_STACK_EXACT.of(itemStack));
        if (!slot.hasChildren()) {
            return Optional.of((new ArrayList<>(slot.getProperties(SlotIndex.class)).get(0)));
        }

        return Optional.empty();
    }

    public static Optional<SlotIndex> getHeldItemSlot(final Player player, HandType hand){
        Optional<ItemStack> op = player.getItemInHand(hand);
        return op.isPresent() ? getSlotIndex(player.getItemInHand(hand).get(), player) : Optional.empty();
    }

    public static Optional<Slot> getSlot(int index, Player player){
        Iterator slots = player.getInventory().slots().iterator();
        while(slots.hasNext()){
            Slot slot = (Slot) slots.next();
            if(!slot.hasChildren()){
                SlotIndex sl = (new ArrayList<>(slot.getProperties(SlotIndex.class)).get(0));
                if(sl.getValue() == index){
                    return Optional.of(slot);
                }
            }
        }
        return Optional.empty();
    }

}
