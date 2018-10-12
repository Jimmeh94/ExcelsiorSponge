package com.excelsiormc.excelsiorsponge.game.inventory;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonSacrificeCards;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel.HotbarHand;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.mcsimonflash.sponge.teslalibs.inventory.Action;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SummonSacrificeInventory {

    private CardBase owner;
    private List<CardBase> sacrifice;
    private Layout layout;
    private View view;
    private int need;
    private ItemStack accept;
    private boolean sacrificed = false;

    public SummonSacrificeInventory(CardBase owner, int needed) {
        this.owner = owner;
        sacrifice = new CopyOnWriteArrayList<>();
        this.need = needed;

        display();
    }

    private void display(){
        List<CardBase> cards = DuelUtils.getArena(owner.getOwner()).get().getGrid().getActiveCardsOnFieldFor(owner.getOwner());

        Layout.Builder builder = Layout.builder();
        builder.dimension(InventoryDimension.of(9, 5));

        int index = 9;
        //9 - 17
        for(CardBase card: cards){
            if(card instanceof CardBaseMonster){
                builder.set(Element.of(card.getFaceupItem(), new Consumer<Action.Click>() {
                    @Override
                    public void accept(Action.Click click) {
                        if(!sacrifice.contains(card)) {
                            sacrifice.add(card);
                            List<Text> temp;
                            if(accept.get(Keys.ITEM_LORE).isPresent()){
                                temp = accept.get(Keys.ITEM_LORE).get();
                            } else {
                                temp = new ArrayList<>();
                            }
                            temp.add(Text.builder().append(Text.of(TextColors.GRAY, " - "), card.getName()).build());
                            accept.offer(Keys.ITEM_LORE, temp);
                            Messager.sendMessage(PlayerUtils.getPlayer(owner.getOwner()).get(),
                                    Text.builder().append(card.getName()).append(Text.of(TextColors.GREEN, " added to sacrifice!")).build(),
                                    Messager.Prefix.DUEL);
                        } else {
                            sacrifice.remove(card);
                            List<Text> temp = accept.get(Keys.ITEM_LORE).get();
                            temp.remove(Text.builder().append(Text.of(TextColors.GRAY, " - "), card.getName()).build());
                            accept.offer(Keys.ITEM_LORE, temp);
                            Messager.sendMessage(PlayerUtils.getPlayer(owner.getOwner()).get(),
                                    Text.builder().append(card.getName()).append(Text.of(TextColors.RED, " removed from sacrifice!")).build(),
                                    Messager.Prefix.DUEL);
                        }

                        view.setElement(18 + 3, getAcceptButton());
                    }
                }), index);

                index++;
            }
        }

        //Continue with sacrifice
        accept = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        accept.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Sacrifice Cards and Finish Summon"));
        builder.set(getAcceptButton(), 18 + 3);

        //cancel
        ItemStack cancel = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        cancel.offer(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "Cancel Summon and Return to Hand"));
        builder.set(Element.of(cancel, new Consumer<Action.Click>() {
                        @Override
                        public void accept(Action.Click click) {
                            Player player = PlayerUtils.getPlayer(owner.getOwner()).get();
                            player.closeInventory();
                            (new HotbarHand(DuelUtils.getCombatProfilePlayer(owner.getOwner()).get())).setHotbar(player);
                            ((SummonSacrificeCards)owner.getSummonType()).resetInventory();
                        }
                    }),
                26 - 3);

        View view = View.builder().archetype(InventoryArchetypes.CHEST)
                                    .property(InventoryTitle.of(Text.of("Choose " + need +" card(s) to sacrifice")))
                                    .onClose(new Consumer<Action<InteractInventoryEvent.Close>>() {
                                        @Override
                                        public void accept(Action<InteractInventoryEvent.Close> closeAction) {
                                            if(owner.isOwnerPlayer() && !sacrificed){
                                                (new HotbarHand(DuelUtils.getCombatProfilePlayer(owner.getOwner()).get()))
                                                        .setHotbar(PlayerUtils.getPlayer(owner.getOwner()).get());

                                                ((SummonSacrificeCards)owner.getSummonType()).resetInventory();
                                            }
                                        }
                                    })
                                    .build(ExcelsiorSponge.PLUGIN_CONTAINER);

        layout = builder.build();
        view.update(layout);
        view.open(PlayerUtils.getPlayer(owner.getOwner()).get());
        this.view = view;
    }

    private Element getAcceptButton(){
        return Element.of(accept, new Consumer<Action.Click>() {
            @Override
            public void accept(Action.Click click) {
                Player player = PlayerUtils.getPlayer(owner.getOwner()).get();
                if(sacrifice.size() >= need){
                    sacrificed = true;
                    player.closeInventory();
                    player.getInventory().clear();
                    ((SummonSacrificeCards)owner.getSummonType()).finishSummon(sacrifice);
                } else {
                    Messager.sendMessage(player, Text.of(TextColors.RED, "You need to sacrifice " + (need - sacrifice.size()) + " more cards!"), Messager.Prefix.DUEL);
                }
            }
        });
    }


}
