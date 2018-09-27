package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.game.user.scoreboard.ArenaDefaultPreset;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarActiveTurn extends Hotbar {

    @Override
    public void setHotbar(Player player) {
        super.setHotbar(player);

        UserPlayer user = PlayerUtils.getUserPlayer(player).get();
        user.changeScoreboardPreset(new ArenaDefaultPreset(user, ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player).get()));
    }

    @Override
    protected void setupItems() {
        Pair<ItemStack, Callback> action;

        /**
         * Need a free-cam mode to look around field, look at hand, look at discard pile
         * 2 = use passive
         * 4 = look at hand
         * 6 = discard pile
         * 8 = options menu (skip turn, leave game)
         */
        ItemStack item = ItemStack.builder().itemType(ItemTypes.BLAZE_POWDER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Use Ability"));

        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType hand) {
                displayAbilityBar();
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayAbilityBar();
            }

            private void displayAbilityBar(){
                //bring up ability hotbar
            }
        });
        addPair(0, action);

        item = ItemStack.builder().itemType(ItemTypes.IRON_BOOTS).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Move a Card"));
        action = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                //Needs to be aiming at a cell with a card owned by this player
                //Brings up hotbar about that card
                CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get();
                if(!cpp.getCurrentAim().isPresent() || cpp.getCurrentAim().get().getOccupyingCard() == null){
                    return;
                }

                if(!cpp.getCurrentAim().get().getOccupyingCard().getMovement().canMoveThisTurn()){
                    Messager.sendMessage(player, Text.of(TextColors.RED, "That card can't move this turn"), Messager.Prefix.ERROR);
                    return;
                }

                (new HotbarCardManipulate(cpp.getCurrentAim().get().getOccupyingCard())).setHotbar(player);
                PlayerUtils.getUserPlayer(player.getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_MOVING_CARD);
                cpp.setCurrentlyMovingCard(cpp.getCurrentAim().get().getOccupyingCard());
            }

            @Override
            public void actionRightClick(Player player, HandType hand){

            }
        });
        addPair(2, action);

        item = ItemStack.builder().itemType(ItemTypes.MAP).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "View Hand"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayhand(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayhand(player);
            }

            private void displayhand(Player player){
                CombatantProfilePlayer temp = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get();
                UserPlayer userPlayer = PlayerUtils.getUserPlayer(player).get();
                userPlayer.setCurrentHotbar(new HotbarHand(temp));
                userPlayer.getCurrentHotbar().setHotbar(player);
            }
        });
        addPair(4, action);

        item = ItemStack.builder().itemType(ItemTypes.BARRIER).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "View Discard Pile"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayDiscardPile(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayDiscardPile(player);
            }

            private void displayDiscardPile(Player player){
                //TODO load dyanmic inventory
            }
        });
        addPair(6, action);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Options Menu"));
        action = new Pair(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                loadOptions(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                loadOptions(player);
            }

            private void loadOptions(Player player){
                //TODO load options hotbar
            }
        });
        addPair(8, action);
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
