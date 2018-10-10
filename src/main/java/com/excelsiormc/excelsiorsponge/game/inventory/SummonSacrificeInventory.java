package com.excelsiormc.excelsiorsponge.game.inventory;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
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
import java.util.function.Consumer;

public class SummonSacrificeInventory {

    private CardBase card;

    public SummonSacrificeInventory(CardBase card) {
        this.card = card;

        display();
    }

    private void display(){
        List<CardBase> cards = DuelUtils.getArena(card.getOwner()).get().getGrid().getActiveCardsOnFieldFor(card.getOwner());

        Layout.Builder builder = Layout.builder();
        builder.dimension(InventoryDimension.of(9, 5));
        builder.border(Element.of(ItemStack.of(ItemTypes.STAINED_GLASS_PANE)));
        for(int i = 0; i < 9; i++){
            builder.set(Element.of(cards.get(i).getFaceupItem()), i + 9);
        }

        builder.set(Element.of(ItemStack.of(ItemTypes.CLAY_BALL), new Consumer<Action.Click>() {
            @Override
            public void accept(Action.Click click) {

            }
        }), 27 + 3);

        builder.set(Element.of(ItemStack.of(ItemTypes.CLAY_BALL), new Consumer<Action.Click>() {
                        @Override
                        public void accept(Action.Click click) {
                            (new HotbarHand(DuelUtils.getCombatProfilePlayer(card.getOwner()).get()))
                                    .setHotbar(PlayerUtils.getPlayer(card.getOwner()).get());
                        }
                    }),
                35 - 3);

        View view = View.builder().archetype(InventoryArchetypes.CHEST)
                                    .property(InventoryTitle.of(Text.of("Choose cards to sacrifice")))
                                    .onClose(new Consumer<Action<InteractInventoryEvent.Close>>() {
                                        @Override
                                        public void accept(Action<InteractInventoryEvent.Close> closeAction) {
                                            if(card.isOwnerPlayer()){
                                                (new HotbarHand(DuelUtils.getCombatProfilePlayer(card.getOwner()).get()))
                                                        .setHotbar(PlayerUtils.getPlayer(card.getOwner()).get());
                                            }
                                        }
                                    })
                                    .build(ExcelsiorSponge.PLUGIN_CONTAINER);
        view.update(builder.build());
        view.open(PlayerUtils.getPlayer(card.getOwner()).get());
    }


}
