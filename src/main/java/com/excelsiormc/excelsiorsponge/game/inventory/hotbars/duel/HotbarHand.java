package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

public class HotbarHand extends Hotbar {

    private CombatantProfilePlayer profile;

    public HotbarHand(CombatantProfilePlayer profile) {
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
                public void action(Player player, HandType action) {
                    if(ExcelsiorSponge.INSTANCE.getArenaManager().findArenaWithPlayer(player).get().getGamemode().getStage()
                            != Gamemode.Stage.IN_GAME){
                        return;
                    }

                    int index = InventoryUtils.getHeldItemSlot(player, action).get().getValue();

                    if(action == HandTypes.MAIN_HAND) {
                        //Lay card on field
                        Cell currentAim = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get().getCurrentAim();

                        if (currentAim != null && currentAim.isAvailable() && profile.getHand().hasCardAt(index)) {
                            currentAim.placeCard(profile.getHand().getCard(index));
                            InventoryUtils.getSlot(index, player).get().clear();
                            setHotbar(player);
                        }
                    } else if(action == HandTypes.OFF_HAND){
                        //Display client side in front of player
                        if(profile.getHand().hasCardAt(index)){
                            Location display = player.getLocation().copy();
                            Vector3d direction = display.getPosition();
                            display.add(2 * direction.getX(), 0, 2 * direction.getZ());
                            profile.getHand().viewCard(index).displayCardDescription(display);

                            UserPlayer userPlayer = (UserPlayer) ExcelsiorCore.INSTANCE.getPlayerBaseManager().getPlayerBase(player.getUniqueId()).get();
                            HotbarCardDescription hotbar = new HotbarCardDescription(profile.getHand().viewCard(index), userPlayer.getCurrentHotbar());
                            hotbar.setHotbar(player);
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
            public void action(Player player, HandType action) {
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
            public void action(Player player, HandType action) {
                if(ExcelsiorSponge.INSTANCE.getArenaManager().findArenaWithPlayer(player).get().isPlayersTurn(player)){
                    Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                } else {
                    Hotbars.HOTBAR_WAITING_TURN.setHotbar(player);
                }
            }
        });
        addPair(8, card);
    }
}
