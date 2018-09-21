package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.CustomEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatChannelManager;
import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerEvents {

    @Listener
    public void onJoin(ClientConnectionEvent.Join event){
        event.setMessageCancelled(true);

        UserPlayer userPlayer = new UserPlayer(event.getTargetEntity());
        ExcelsiorCore.INSTANCE.getPlayerBaseManager().add(userPlayer);
        ChatChannelManager.GLOBAL.addMember(userPlayer);
        //TODO set default hotbar
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event){
        event.setMessageCancelled(true);

        ExcelsiorSponge.INSTANCE.getArenaManager().checkPlayer(event.getTargetEntity());
        UserPlayer userPlayer = PlayerUtils.getUserPlayer(event.getTargetEntity()).get();
        ExcelsiorCore.INSTANCE.getPlayerBaseManager().remove(userPlayer);
        ExcelsiorCore.INSTANCE.getChannelManager().removePlayerFromAllChannels(userPlayer);
    }

    @Listener
    public void onInteract(InteractBlockEvent event, @Root Player player){
        event.setCancelled(true);

        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player).get();
        if(event instanceof InteractBlockEvent.Primary){
            if(userPlayer.isInDuel()){
                userPlayer.getCurrentHotbar().handle(player, ((InteractBlockEvent.Primary) event).getHandType());
            }
        } else {
            if(userPlayer.isInDuel()){
                userPlayer.getCurrentHotbar().handle(player, ((InteractBlockEvent.Secondary) event).getHandType());
            }
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
