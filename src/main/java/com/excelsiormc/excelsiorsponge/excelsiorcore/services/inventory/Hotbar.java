package com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory;

import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;

import java.util.HashMap;
import java.util.Map;

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
        Inventory hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(org.spongepowered.api.item.inventory.entity.Hotbar.class));
        hotbar.clear();

        Slot slot;
        for(int i = 0; i < 9; i++){
            slot = hotbar.next();
            if(items.containsKey(i)){
                slot.set(items.get(i).getFirst());
            }
        }
        //player.updateInventory();

        ExcelsiorCore.INSTANCE.getPlayerBaseManager().getPlayerBase(player.getUniqueId()).get().setCurrentHotbar(this);
    }

    public void handle(int index, Player player, HandType handClick) {
        if(items.get(index) != null && items.get(index).getSecond() != null){
            items.get(index).getSecond().action(player, handClick);
        }
    }

    public interface Callback{

        void action(Player player, HandType action);
    }

}
