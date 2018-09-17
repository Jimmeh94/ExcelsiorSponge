package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.CardBase;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * This hotbar is for when a player is viewing a card from their hand
 */
public class HotbarCardDescription extends Hotbar {

    private CardBase cardBase;
    private Hotbar previous;

    public HotbarCardDescription(CardBase cardBase, Hotbar previous) {
        this.cardBase = cardBase;
        this.previous = previous;

        setupItems();
    }

    @Override
    protected void setupItems() {
        if(cardBase == null){
            return;
        }

        Pair<ItemStack, Callback> card;

        ItemStack item = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Card Description"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void action(Player player, HandType action) {
                Message.Builder builder = Message.builder();
                builder.addReceiver(player);
                builder.addMessage(Text.of(" "));
                builder.addMessage(Text.of(TextColors.GRAY + "[===== " + cardBase.getName() + TextColors.GRAY + " =====]"));
                builder.addMessage(Text.of(" "));

                for(Text s: cardBase.getMesh().getValue(Keys.ITEM_LORE).get()){
                    builder.addAsChild(s, TextColors.GOLD);
                }
                builder.addMessage(Text.of(" "));

                builder.addMessage(Text.of(TextColors.GRAY + "[=====================================]"));
                Messager.sendMessage(builder.build());
            }
        });
        addPair(3, card);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Return to Previous Hotbar"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void action(Player player, HandType action) {
                previous.setHotbar(player);
            }
        });
        addPair(6, card);
    }
}
