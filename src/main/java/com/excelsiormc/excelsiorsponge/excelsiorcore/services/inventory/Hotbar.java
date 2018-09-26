package com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory;

import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.utils.ClickTypes;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Hotbar {

    protected Map<Integer, Pair<ItemStack, Callback>> items;

    public Hotbar() {
        items = new HashMap<>();

        setupItems();
    }

    protected void clearItems(){
        items.clear();
    }

    protected abstract void setupItems();
    public abstract void handleEmptyRightClick(Player player);
    public abstract void handleEmptyLeftClick(Player player);

    protected void addItem(int index, ItemStack item){
        items.put(index, new Pair<>(item, null));
    }

    protected void addItemWithAction(int index, ItemStack item, Callback callback){
        items.put(index, new Pair<>(item, callback));
    }

    protected void addPair(int index, Pair<ItemStack, Callback> card){
        items.put(index, card);
    }

    public Map<Integer, Pair<ItemStack, Callback>> getItems() {
        return items;
    }

    public void setHotbar(Player player){
        final Inventory hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(org.spongepowered.api.item.inventory.entity.Hotbar.class));
        hotbar.clear();

        int i = 0;
        Slot slot;

        for (final Inventory temp : hotbar.slots()) {
            slot = (Slot) temp;
            if(items.containsKey(i)){
                slot.set(items.get(i).getFirst());
            }
            i++;
        }

        ExcelsiorCore.INSTANCE.getPlayerBaseManager().getPlayerBase(player.getUniqueId()).get().setCurrentHotbar(this);
    }

    public void handle(Player player, HandType handClick, ClickTypes clickType) {
        Optional<SlotIndex> slotIndex = InventoryUtils.getHeldItemSlot(player, handClick);

        if(!slotIndex.isPresent()){
            return;
        }

        int index = slotIndex.get().getValue();

        if(items.get(index) != null && items.get(index).getSecond() != null){
            items.get(index).getSecond().performAction(clickType, player, handClick);
        } else {
            if (clickType == ClickTypes.LEFT) {
                handleEmptyLeftClick(player);
            } else {
                handleEmptyRightClick(player);
            }
        }
    }

    public abstract class Callback{

        public void performAction(ClickTypes clickType, Player player, HandType handType){

            if(clickType ==  ClickTypes.LEFT){
                actionLeftClick(player, handType);
            } else if(clickType == ClickTypes.RIGHT){
                actionRightClick(player, handType);
            }
        }

        protected abstract void actionLeftClick(Player player, HandType hand);
        protected abstract void actionRightClick(Player player, HandType hand);
    }

}
