package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarWaitingTurn extends Hotbar {

    @Override
    protected void setupItems() {
        Pair<ItemStack, Callback> action;

        ItemStack item = ItemStack.builder().itemType(ItemTypes.BLAZE_POWDER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "View Abilities"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                displayAbilityBar();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayAbilityBar();
            }

            private void displayAbilityBar(){

            }
        });
        addPair(0, action);

        item = ItemStack.builder().itemType(ItemTypes.MAP).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "View Hand"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayhand(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayhand(player);
            }

            private void displayhand(Player player){
                CombatantProfilePlayer temp = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get();
                UserPlayer userPlayer = PlayerUtils.getUserPlayer(player).get();
                userPlayer.setCurrentHotbar(new HotbarHandDummy(temp));
                userPlayer.getCurrentHotbar().setHotbar(player);
            }
        });
        addPair(4, action);

        item = ItemStack.builder().itemType(ItemTypes.BARRIER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "View Discard Pile"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayDiscardPile(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayDiscardPile(player);
            }

            private void displayDiscardPile(Player player){
                //TODO load dyanmic inventory
            }
        });
        addPair(6, action);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Options Menu"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                loadOptions(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                loadOptions(player);
            }

            private void loadOptions(Player player){
                Hotbars.HOTBAR_DUEL_OPTIONS_WAITING_TURN.setHotbar(player);
            }
        });
        addPair(8, action);
    }

    @Override
    public void handleEmptyRightClick(Player player) {
        DuelUtils.displayCellInfo(player);
    }

    @Override
    public void handleEmptyLeftClick(Player player) {
        DuelUtils.displayCellInfo(player);
    }
}
