package com.excelsiormc.excelsiorsponge.game.inventory.hotbars;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.field.GridNormal;
import com.excelsiormc.excelsiorsponge.game.match.matchmaking.Queues;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarDefault extends Hotbar {

    @Override
    protected void setupItems() {
        Pair<ItemStack, Callback> action;

        /**
         * 4 = matchmaker
         */
        ItemStack item;

        item = ItemStack.builder().itemType(ItemTypes.BLAZE_POWDER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Create Arena"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                create(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                create(player);
            }

            private void create(Player player){
                ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager()
                        .add(new Arena(new GridNormal(player.getLocation().getPosition(), player.getLocation().getExtent().getName(),
                                11, 11, 5, true), player.getLocation().getExtent().getName()));
            }
        });
        addPair(0, action);

        item = ItemStack.builder().itemType(ItemTypes.BLAZE_POWDER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Join Queue"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                joinQueue(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                joinQueue(player);
            }

            private void joinQueue(Player player){
                ExcelsiorSponge.INSTANCE.getMatchMaker().playerJoinQueue(player, Queues.DUEL);
            }
        });
        addPair(4, action);
    }

    @Override
    public void handleEmptyRightClick(Player player) {

    }

    @Override
    public void handleEmptyLeftClick(Player player) {

    }
}
