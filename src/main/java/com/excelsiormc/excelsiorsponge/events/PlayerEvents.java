package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.CustomEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatChannelManager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.ClickTypes;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PlayerEvents {

    @Listener
    public void onJoin(ClientConnectionEvent.Join event){
        event.setMessageCancelled(true);

        UserPlayer userPlayer = new UserPlayer(event.getTargetEntity());
        ExcelsiorCore.INSTANCE.getPlayerBaseManager().add(userPlayer);
        ChatChannelManager.GLOBAL.addMember(userPlayer);
        Hotbars.HOTBAR_DEFAULT.setHotbar(userPlayer.getPlayer());

        Message message = Message.builder().addReceiver(event.getTargetEntity())
                .addMessage(Text.of(TextColors.GOLD, "How to use this plugin:"))
                .addAsChild(Text.of(TextColors.GRAY, "Use the first item to generate an arena"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "Use the center item to join a queue"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "When there are 2 people in the queue, it'll begin"), TextColors.GOLD)
                .build();

        Messager.sendMessage(message);
        Messager.sendBlankLine(event.getTargetEntity());

        message = Message.builder().addReceiver(event.getTargetEntity())
                .addMessage(Text.of(TextColors.GOLD, "How to play the game:"))
                .addAsChild(Text.of(TextColors.GRAY, "Objective: bring enemy's avatar life to 0"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "You can place cards around your avatar"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "You can place a card face up or face down"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "(You can use this to hide the card's identity from your opponent)"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "You can place cards in attack or defense mode"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "In attack, the Power stat will be used in fights"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "In defense, the Health stat will be used in fights"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "Once a card is placed, you can move it by 'Use Card' on the active turn hotbar"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "Think of this like a cross between chess and a traditional card game"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "Enjoy!"), TextColors.GOLD)
                .build();
        Messager.sendMessage(message);
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event){
        event.setMessageCancelled(true);

        UserPlayer userPlayer = PlayerUtils.getUserPlayer(event.getTargetEntity()).get();
        ExcelsiorCore.INSTANCE.getPlayerBaseManager().remove(userPlayer);
        ExcelsiorCore.INSTANCE.getChannelManager().removePlayerFromAllChannels(userPlayer);
        ExcelsiorSponge.INSTANCE.getMatchMaker().playerLeave(event.getTargetEntity());
    }

    @Listener
    public void onOpenInventory(ClickInventoryEvent event, @First Player player){
        if(event instanceof ClickInventoryEvent.Primary){
            if(event.getTargetInventory() == player.getInventory()){
                event.setCancelled(true);
            }
        } else if(event instanceof ClickInventoryEvent.Secondary){
            if(event.getTargetInventory() == player.getInventory()){
                event.setCancelled(true);
            }

        } else {
            event.setCancelled(true);
        }
    }

    @Listener
    public void onInteract(InteractBlockEvent event, @Root Player player){
        event.setCancelled(true);

        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player).get();
        HandType hand;
        /**
         * For duels:
         * Left clicks will be for actions
         * Right clicks will be for contextual things
         */

        if(event instanceof InteractBlockEvent.Primary.MainHand){
            hand = ((InteractBlockEvent.Primary.MainHand)event).getHandType();
            userPlayer.getCurrentHotbar().handle(player, hand, ClickTypes.LEFT);

        } else if(event instanceof InteractBlockEvent.Secondary.MainHand){
            hand = ((InteractBlockEvent.Secondary.MainHand)event).getHandType();
            userPlayer.getCurrentHotbar().handle(player, hand, ClickTypes.RIGHT);
        }

    }

    @Listener
    public void onBreak(ChangeBlockEvent.Break event, @Root Player player){
        if(player.gameMode().get() != GameModes.CREATIVE){
            event.setCancelled(true);
        }
    }

    public static class PlayerModeChangeEvent extends CustomEvent {

        private UserPlayer.PlayerMode changeFrom, changeTo;
        private Player player;

        public PlayerModeChangeEvent(Cause cause, UserPlayer.PlayerMode changeFrom, UserPlayer.PlayerMode changeTo, Player player) {
            super(cause);
            this.changeFrom = changeFrom;
            this.changeTo = changeTo;
            this.player = player;
        }

        public UserPlayer.PlayerMode getChangeFrom() {
            return changeFrom;
        }

        public UserPlayer.PlayerMode getChangeTo() {
            return changeTo;
        }

        public Player getPlayer() {
            return player;
        }
    }

}
