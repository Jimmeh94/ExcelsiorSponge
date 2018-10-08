package com.excelsiormc.excelsiorsponge.excelsiorcore.services;

import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
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
        final Slot slot = player.getInventory().<Slot>query(QueryOperationTypes.ITEM_STACK_IGNORE_QUANTITY.of(itemStack));
        if (!slot.hasChildren()) {
            return Optional.of((new ArrayList<>(slot.getProperties(SlotPos.class)).get(0)));
        }

        return Optional.empty();
    }

    public static boolean isHandEmpty(Player player, HandType hand){
        return !player.getItemInHand(hand).isPresent() || player.getItemInHand(hand).get().getType() == ItemTypes.AIR;
    }

    public static Optional<SlotIndex> getSlotIndex(final ItemStack itemStack, final Player player) {
        Iterator<Slot> it = player.getInventory().<Slot>slots().iterator();
        while(it.hasNext()){
            Slot slot = it.next();
            if(!slot.hasChildren()){
                if(slot.contains(itemStack)){
                    return Optional.of((new ArrayList<>(slot.getProperties(SlotIndex.class)).get(0)));
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<SlotIndex> getHeldItemSlot(final Player player, HandType hand){
        Optional<ItemStack> op = player.getItemInHand(hand);
        return op.isPresent() ? getSlotIndex(player.getItemInHand(hand).get(), player) : Optional.empty();
    }

    public static Optional<Slot> getSlot(int index, Player player){
        Slot slot = player.getInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).first();

        return slot == null ? Optional.empty() : Optional.of(slot);
    }

    public static void removeItem(ItemStack item, Player player) {
        Iterator<Slot> it = player.getInventory().<Slot>slots().iterator();
        while(it.hasNext()){
            Slot slot = it.next();
            if(slot.contains(item)){
                slot.clear();
            }
        }
    }
}
