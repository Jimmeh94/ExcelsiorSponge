package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarHandDummy extends Hotbar {

    private CombatantProfilePlayer profile;

    public HotbarHandDummy(CombatantProfilePlayer profile) {
        this.profile = profile;
    }

    @Override
    public void setHotbar(Player player) {
        setupItems();

        super.setHotbar(player);
    }

    @Override
    protected void setupItems() {
        if(profile == null){
            return;
        }

        clearItems();

        Pair<ItemStack, Callback> card;

        for(int i = 0; i < profile.getHand().getSize(); i++){
            card = new Pair<ItemStack, Callback>(profile.getHand().viewCard(i).getMesh(), new Callback() {
                @Override
                public void actionLeftClick(Player player, HandType action) {

                }

                @Override
                public void actionRightClick(Player player, HandType hand){
                    //Display card info
                    int index = InventoryUtils.getHeldItemSlot(player, hand).get().getValue();
                    if(profile.getHand().hasCardAt(index)){

                        CardBase card = profile.getHand().viewCard(index);
                        if(card instanceof CardBaseMonster){
                            CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get();
                            if(cpp.getCurrentAim() != null && !cpp.getCurrentAim().get().isAvailable()){
                                cpp.getCurrentAim().get().getOccupyingCard().displayStats(player);
                            }
                        }
                    }
                }
            });
            addPair(i, card);
        }

        ItemStack temp = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).build();
        temp.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Right Click for Help"));

        card = new Pair<ItemStack, Callback>(temp, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                print(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                print(player);
            }

            private void print(Player player){
                Message.Builder builder = Message.builder();
                builder.addReceiver(player);
                builder.addMessage(Text.of(" "));
                builder.addMessage(Text.of(TextColors.GRAY + "====================================="));
                builder.addMessage(Text.of(" "));
                builder.addAsChild(Text.of(TextColors.GOLD, "Left Click with a card in hand to place it on the field"), TextColors.GOLD);
                builder.addAsChild(Text.of(TextColors.GOLD, "Right Click with a card in hand to see the details"), TextColors.GOLD);
                builder.addMessage(Text.of(" "));
                builder.addAsChild(Text.of(TextColors.GOLD, "Left/Right Click towards the field to get info about the target area"), TextColors.GOLD);
                builder.addMessage(Text.of(" "));
                builder.addMessage(Text.of(TextColors.GRAY + "====================================="));
                Messager.sendMessage(builder.build());
            }
        });
        addPair(7, card);

        temp = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        temp.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Game Menu"));
        card = new Pair<>(temp, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                action(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                action(player);
            }

            private void action(Player player){
                Arena arena = DuelUtils.findArenaWithPlayer(player).get();
                if(arena.isPlayersTurn(player)){
                    Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                    DuelUtils.getTeam(player.getUniqueId()).eraseAsPlaceableRows(player);
                } else {
                    Hotbars.HOTBAR_WAITING_TURN.setHotbar(player);
                }
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
        DuelUtils.displayCellInfo(player);
    }
}
