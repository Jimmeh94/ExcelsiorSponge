package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.CustomEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatChannelManager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.ClickTypes;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
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
        Hotbars.HOTBAR_DEFAULT.setHotbar(userPlayer.getPlayer());
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

    /*@Listener
    public void onInteractEmpty(InteractEvent event, @Root Player player){
        event.setCancelled(true);
        System.out.println(event.getClass().getCanonicalName());
        System.out.println("====================");

        UserPlayer userPlayer = PlayerUtils.getUserPlayer(player).get();

        //This is an empty right hand
        userPlayer.getCurrentHotbar().handle(player, HandTypes.MAIN_HAND, ClickTypes.RIGHT);
    }*/

    @Listener
    public void onBreak(ChangeBlockEvent.Break event, @Root Player player){
        if(player.gameMode().get() != GameModes.CREATIVE){
            event.setCancelled(true);
        }
    }

    @Listener
    public void onModeChange(PlayerModeChangeEvent event){
        if(ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(event.getPlayer()).isPresent()){
            CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(event.getPlayer().getUniqueId()).get();

            if(event.getChangeTo() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                if(cpp.getCurrentAim() != null){
                    cpp.getCurrentAim().clearAimForPlayer(event.getPlayer());
                }

                if(event.getChangeTo() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                    //highlight all available cells to move to as green
                    if(cpp.getCurrentAim() != null){
                        ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(event.getPlayer().getUniqueId()).get()
                                .getGrid().displayAvailableCellsToMoveTo(cpp.getCurrentAim().getOccupyingCard());
                    }
                }
            } else if(event.getChangeTo() == UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT){
                if(cpp.getCurrentAim() != null){
                    cpp.getCurrentAim().drawAimForPlayer(event.getPlayer());
                }
                if(event.getChangeFrom() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                    for(Cell cell: ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(event.getPlayer()).get().getGrid().getCells()){
                        if(!cell.isAvailable() && cell.getOccupyingCard().isOwner(event.getPlayer().getUniqueId())){
                            ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(event.getPlayer()).get()
                                    .getGrid().eraseAvailableCellsToMoveTo(cell.getOccupyingCard());
                        }
                    }
                }
            }
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
