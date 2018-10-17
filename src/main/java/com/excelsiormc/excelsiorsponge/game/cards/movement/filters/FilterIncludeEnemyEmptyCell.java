package com.excelsiormc.excelsiorsponge.game.cards.movement.filters;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseAvatar;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.timers.DelayedOneUseTimer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class FilterIncludeEnemyEmptyCell extends FilterIncludeEmptyCell {

    @Override
    public void filter(List<Cell> cells) {
        super.filter(cells);

        Team team = DuelUtils.getTeam(owner.getOwner());
        for(Cell cell: cells){
            if(!cell.isAvailable() && !team.isCombatant(cell.getOccupyingCard().getOwner())){
                addCell(cell);
            }
        }

        Arena arena = DuelUtils.getArena(owner.getOwner()).get();
        for(Cell cell: applicableCells){
            Row row = arena.getGrid().getRowBetweenCells(owner.getCurrentCell(), cell);
            int index = -1;

            for(Cell c: row.getCells()){
                if(index == -1 && DuelUtils.isCellEnemyOccupied(c, arena.getGamemode().getTeamWithCombatant(owner.getOwner()))){
                    index = row.getCells().indexOf(c);
                }
            }

            if(index != -1) {
                if (!row.getCells().subList(0, index + 1).contains(cell)) {
                    applicableCells.remove(cell);
                }
            }
        }

        for(Cell cell: applicableCells){
            if(DuelUtils.isCellEnemyOccupied(cell, arena.getGamemode().getTeamWithCombatant(owner.getOwner()))){
                if(cell.getOccupyingCard().hasKey(CardBase.CardKeys.DONT_DRAW_CELL_FOR_ENEMY)){
                    applicableCells.remove(cell);
                }
            }
        }
    }

    @Override
    public void action(Cell target) {
        if(target.isAvailable()){
            super.action(target);
            return;
        }

        Arena arena = DuelUtils.getArena(owner.getOwner()).get();
        Player player = PlayerUtils.getPlayer(owner.getOwner()).get();

        //Move card to cell right before the target enemy
        if(arena.getGrid().getDistanceBetweenCells(owner.getCurrentCell(), target) > 1){
            Row row = arena.getGrid().getRowBetweenCells(owner.getCurrentCell(), target);
            DuelUtils.moveCardToCell(row.getCells().get(row.getCells().size() - 2), player);

            CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
            cpp.stopMovingCard();

            arena.getGamemode().setTimePaused(true);

            new DelayedOneUseTimer(10L) {
                @Override
                protected void runTask() {
                    battle(arena, target, player);
                    arena.getGamemode().setTimePaused(false);
                }
            };
        } else {
            battle(arena, target, player);
        }
    }

    private void battle(Arena arena, Cell target, Player player){
        BattleResult battleResult = arena.getGamemode().battle(owner.getCurrentCell(), target);

        if(battleResult.getVictorCard().isPresent()){
            if(battleResult.getVictorCard().get() == owner && !(battleResult.getLoserCard().get() instanceof CardBaseAvatar)
                && battleResult.isDefenderDestroyed()){
                CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
                cpp.setCurrentlyMovingCard(owner);
                DuelUtils.moveCardToCell(target, player);
            }
        }
    }

    @Override
    public void drawCells(Player player) {
        for(Cell cell: getApplicableCells()){
            if(cell.isAvailable()){
                cell.drawCustomEmpty(player);
            } else {
                cell.drawCustomEnemyCurrentThreat(player);
            }
        }
    }
}
