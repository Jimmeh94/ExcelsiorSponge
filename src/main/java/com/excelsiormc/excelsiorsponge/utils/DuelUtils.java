package com.excelsiormc.excelsiorsponge.utils;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

public class DuelUtils {

    public static void displayCellInfo(Player player){
        ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player).get().handlePlayerEmptyClick(player);
    }

    public static void moveCardToCell(Player player){
        CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get();
        moveCardToCell(cpp.getCurrentAim().get(), player);
    }

    public static void moveCardToCell(Cell target, Player owner){
        CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(owner.getUniqueId()).get();
        CardBase card = cpp.getCurrentlyMovingCard();

        if(target != null && card != null){
            Cell old = card.getCurrentCell();
            old.setAvailable(true);
            target.setOccupyingCard(card, false);
            card.move(target.getCenterCeiling(), old);
        }
    }

    public static Cell getCellWithoutTerrainOrOverrideable(CellTerrain terrain, Grid grid){
        Cell start = grid.getRandomCell();

        while(start.getCellType() != null && !start.getCellType().canBeOverriddenBy(terrain)){
            start = grid.getRandomCell();
        }

        return start;
    }

    public static Optional<Arena> getArena(UUID uuid){
        return ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(uuid);
    }

    public static boolean isCellEnemyOccupied(Cell cell, Team ally){
        return !cell.isAvailable() && !ally.isCombatant(cell.getOccupyingCard().getOwner());
    }

}
