package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarDuelOptionsCurrentTurn extends Hotbar {
    @Override
    protected void setupItems() {
        Pair<ItemStack, Callback> action;

        /**
         * Need a free-cam mode to look around field, look at hand, look at discard pile
         * 0 = edit settings
         * 2 = vote to end turn
         * 4 = return to active turn hotbar
         * 8 = surrender
         */
        ItemStack item = ItemStack.builder().itemType(ItemTypes.IRON_PICKAXE).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Edit Your Settings"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                editSettings();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                editSettings();
            }

            private void editSettings(){

            }
        });
        addPair(0, action);

        item = ItemStack.builder().itemType(ItemTypes.BARRIER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "End Turn"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                endTurn(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                endTurn(player);
            }

            private void endTurn(Player player){
                DuelUtils.getArena(player.getUniqueId()).get().getGamemode().voteToEndTurn(player);
                if(DuelUtils.isActiveTurn(player)) {
                    Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                } else {
                    Hotbars.HOTBAR_WAITING_TURN.setHotbar(player);
                }
            }
        });
        addPair(2, action);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Return to Main Hotbar"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                displayActiveTurnHotbar(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayActiveTurnHotbar(player);
            }

            private void displayActiveTurnHotbar(Player player){
                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
            }
        });
        addPair(4, action);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Surrender"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                displayConfirmationWindow();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayConfirmationWindow();
            }

            private void displayConfirmationWindow(){

            }
        });
        addPair(8, action);
    }

    @Override
    public void handleEmptyRightClick(Player player) {

    }

    @Override
    public void handleEmptyLeftClick(Player player) {

    }
}
