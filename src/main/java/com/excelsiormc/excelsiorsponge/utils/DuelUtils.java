package com.excelsiormc.excelsiorsponge.utils;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import org.spongepowered.api.entity.living.player.Player;

public class DuelUtils {

    public static void displayCellInfo(Player player){
        ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player).get().handlePlayerEmptyClick(player);
    }

    public static void moveCardToCell(Player player){
        CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get();
        Cell aim = cpp.getCurrentAim().get();
        CardBase card = cpp.getCurrentlyMovingCard();

        if(aim != null){
            if(card != null){
                Cell old = card.getCurrentCell();
                old.setAvailable(true);
                aim.setOccupyingCard(card, false);
                card.moveArmorStand(aim.getCenter(), old);

                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                PlayerUtils.getUserPlayer(player.getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
                PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get().setCurrentlyMovingCard(null);
            }
        }
    }

}
