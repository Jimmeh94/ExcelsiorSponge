package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarCardEdit extends Hotbar {

    private CardBase card;

    public HotbarCardEdit(CardBase card) {
        this.card = card;

        setupItems();
    }

    @Override
    protected void setupItems() {
        if(card == null){
            return;
        }

        Pair<ItemStack, Callback> action;
        ItemStack item;

        /**
         * 0 = activate card ability
         * 2 = move card
         * 4 = change card stance
         * 6 = flip card
         * 8 = cancel
         */

        if(card.getActive().isPresent()) {
            item = ItemStack.builder().itemType(ItemTypes.BLAZE_POWDER).build();
            item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Use Card Ability"));

            action = new Pair<>(item, new Callback() {
                @Override
                public void actionLeftClick(Player player, HandType hand) {
                    useAbility();
                }

                @Override
                public void actionRightClick(Player player, HandType hand) {
                    useAbility();
                }

                private void useAbility() {
                    card.getActive().get().action();
                }
            });
            addPair(0, action);
        }

        item = ItemStack.builder().itemType(ItemTypes.IRON_BOOTS).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Move Card"));
        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                //Needs to be aiming at a cell with a card owned by this player
                //Brings up hotbar about that card
                CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get();

                if(!card.getMovement().canMoveThisTurn()){
                    Messager.sendMessage(player, Text.of(TextColors.RED, "That card can't move this turn"), Messager.Prefix.ERROR);
                    return;
                }

                if(!card.generateSpots()){
                    Messager.sendMessage(player, Text.of(TextColors.RED, "That card can't move anywhere"), Messager.Prefix.ERROR);
                    return;
                }

                (new HotbarCardManipulate(card)).setHotbar(player);
                cpp.startMovingCard(card);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){

            }
        });
        addPair(2, action);

        item = ItemStack.builder().itemType(ItemTypes.IRON_SWORD).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Change Card Stance"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                changeStance(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                changeStance(player);
            }

            private void changeStance(Player player){
                card.toggleCardPosition();
            }
        });
        addPair(4, action);

        item = ItemStack.builder().itemType(ItemTypes.SHIELD).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Flip Face Down Card"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                flip(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                flip(player);
            }

            private void flip(Player player){
                if(card.isFaceDown()){
                    card.flipCard();
                } else {
                    Messager.sendMessage(player, Text.of(TextColors.RED, "This card is already face up!"), Messager.Prefix.DUEL);
                }
            }
        });
        addPair(6, action);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Cancel"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                cancel(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                cancel(player);
            }

            private void cancel(Player player){
                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
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
