package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class HotbarCardManipulate extends Hotbar {

    private CardBase cardBase;

    public HotbarCardManipulate(CardBase cardBase) {
        this.cardBase = cardBase;

        setupItems();
    }

    @Override
    protected void setupItems() {
        if(cardBase == null){
            return;
        }

        Pair<ItemStack, Callback> card;
        ItemStack item = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Right Click to use CardEvent Ability"));

        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                //Activate card passive ability
            }

            @Override
            public void actionRightClick(Player player, HandType hand){

            }
        });
        addPair(1, card);

        item = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "CardEvent Description"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayDescription(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayDescription(player);
            }

            private void displayDescription(Player player){
                Message.Builder builder = Message.builder();
                builder.addReceiver(player);
                builder.addMessage(Text.of(" "));
                builder.addMessage(Text.of(TextColors.GRAY + "[===== " + cardBase.getName() + TextColors.GRAY + " =====]"));
                builder.addMessage(Text.of(" "));

                for(Text s: cardBase.getLore()){
                    builder.addAsChild(s, TextColors.GOLD);
                }
                builder.addMessage(Text.of(" "));

                builder.addMessage(Text.of(TextColors.GRAY + "[=====================================]"));
                Messager.sendMessage(builder.build());
            }
        });
        addPair(6, card);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Return to Duel Menu"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                loadMenu(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                loadMenu(player);
            }

            private void loadMenu(Player player){
                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get().stopMovingCard();
            }
        });
        addPair(8, card);
    }

    @Override
    public void handleEmptyRightClick(Player player) {
        DuelUtils.displayCellInfo(player);
    }

    @Override
    public void handleEmptyLeftClick(Player player) {
        //If aiming at appropriate cell for the card to move to, move card
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get();
        Optional<Cell> aim = cpp.getCurrentAim();
        CardBase card = cpp.getCurrentlyMovingCard();

        if(aim.isPresent()){
            if(card != null){
                if(card.getMovement().isAvailableSpace(aim.get())){
                    card.getMovement().handle(aim.get());
                    Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                    DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get().stopMovingCard();
                }
            }
        }
    }
}
