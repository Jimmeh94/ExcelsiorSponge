package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.game.user.scoreboard.ArenaDefaultPreset;
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
        user.changeScoreboardPreset(new ArenaDefaultPreset(user, ExcelsiorSponge.INSTANCE.getArenaManager().findArenaWithPlayer(player).get()));
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
            public void action(Player player, HandType action) {
                //bring up ability hotbar
            }
        });
        addPair(0, action);

        item = ItemStack.builder().itemType(ItemTypes.IRON_BOOTS).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Move a Card"));
        action = new Pair<>(item, new Callback() {
            @Override
            public void action(Player player, HandType action) {
                //Needs to be aiming at a cell with a card owned by this player
                //Brings up hotbar about that card
                CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get();
                if(cpp.getCurrentAim() == null || cpp.getCurrentAim().getOccupyingCard() == null){
                    return;
                }

                (new HotbarCardManipulate(cpp.getCurrentAim().getOccupyingCard())).setHotbar(player);
                PlayerUtils.getUserPlayer(player.getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_MOVING_CARD);
                cpp.setCurrentlyMovingCard(cpp.getCurrentAim().getOccupyingCard());
            }
        });
        addPair(2, action);

        item = ItemStack.builder().itemType(ItemTypes.MAP).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "View Hand"));
        action = new Pair(item, new Callback() {
            @Override
            public void action(Player player, HandType action) {

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
            public void action(Player player, HandType action) {
                //TODO load dyanmic inventory
            }
        });
        addPair(6, action);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Options Menu"));
        action = new Pair(item, new Callback() {
            @Override
            public void action(Player player, HandType action) {
                //TODO load options hotbar
            }
        });
        addPair(8, action);
    }
}
