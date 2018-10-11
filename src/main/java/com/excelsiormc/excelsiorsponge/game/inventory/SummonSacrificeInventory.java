package com.excelsiormc.excelsiorsponge.game.inventory;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
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
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SummonSacrificeInventory {

    private CardBase owner;
    private List<CardBase> sacrifice;
    private Layout layout;
    private View view;
    private int need;

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
        builder.border(Element.of(ItemStack.of(ItemTypes.CLAY_BALL)));

        int index = 9;
        //9 - 17
        for(CardBase card: cards){
            if(card instanceof CardBaseMonster){
                builder.set(Element.of(card.getFaceupItem(), new Consumer<Action.Click>() {
                    @Override
                    public void accept(Action.Click click) {

                    }
                }), index);

                index++;
            }
        }

        //Continue with sacrifice
        builder.set(Element.of(ItemStack.of(ItemTypes.CLAY_BALL), new Consumer<Action.Click>() {
            @Override
            public void accept(Action.Click click) {
                if(sacrifice.size() >= need){
                    ((SummonSacrificeCards)owner.getSummonType()).finishSummon();
                    PlayerUtils.getPlayer(owner.getOwner()).get().closeInventory();
                }
            }
        }), 27 + 3);

        //cancel
        builder.set(Element.of(ItemStack.of(ItemTypes.CLAY_BALL), new Consumer<Action.Click>() {
                        @Override
                        public void accept(Action.Click click) {
                            (new HotbarHand(DuelUtils.getCombatProfilePlayer(owner.getOwner()).get()))
                                    .setHotbar(PlayerUtils.getPlayer(owner.getOwner()).get());
                        }
                    }),
                35 - 3);

        View view = View.builder().archetype(InventoryArchetypes.CHEST)
                                    .property(InventoryTitle.of(Text.of("Choose " + need +"cards to sacrifice")))
                                    .onClose(new Consumer<Action<InteractInventoryEvent.Close>>() {
                                        @Override
                                        public void accept(Action<InteractInventoryEvent.Close> closeAction) {
                                            if(owner.isOwnerPlayer()){
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


}
