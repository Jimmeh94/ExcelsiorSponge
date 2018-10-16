package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarCardFaceChoice extends Hotbar {

    private CardBase cardBase;

    public HotbarCardFaceChoice(CardBase cardBase) {
        this.cardBase = cardBase;

        setupItems();
    }

    @Override
    protected void setupItems() {
        if(cardBase == null){
            return;
        }

        Pair<ItemStack, Callback> card;
        ItemStack item = ItemStack.builder().itemType(ItemTypes.IRON_SWORD).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Place CardEvent Face Up"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                placeCardFaceUp();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                placeCardFaceUp();
            }

            private void placeCardFaceUp(){
                cardBase.setCardFacePosition(CardBase.CardFacePosition.FACE_UP);
                cardBase.handleSummon();
            }
        });
        addPair(2, card);

        item = ItemStack.builder().itemType(ItemTypes.SHIELD).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Place CardEvent Face Down"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                placeCardFaceDown();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                placeCardFaceDown();
            }

            private void placeCardFaceDown(){
                cardBase.setCardFacePosition(CardBase.CardFacePosition.FACE_DOWN);
                cardBase.handleSummon();
                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(PlayerUtils.getPlayer(cardBase.getOwner()).get());
            }
        });
        addPair(5, card);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Cancel and Return to Hand"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayHand();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayHand();
            }

            private void displayHand(){
                CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(cardBase.getOwner()).get();
                (new HotbarHand(cpp)).setHotbar(cpp.getPlayer());
            }
        });
        addPair(8, card);
    }

    @Override
    public void handleEmptyRightClick(Player player) {

    }

    @Override
    public void handleEmptyLeftClick(Player player) {

    }
}
