package com.excelsiormc.excelsiorsponge.game.cards.movement.filters;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementColors;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.timers.AbstractTimer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class FilterIncludeEnemyCell extends MovementFilter {

    @Override
    public void filter(List<Cell> cells) {
        Team team = PlayerUtils.getTeam(owner.getOwner());
        for(Cell cell: cells){
            if(!cell.isAvailable() && !team.isCombatant(cell.getOccupyingCard().getOwner())){
                addCell(cell);
            }
        }
    }

    @Override
    public void action(Cell target) {
        Arena arena = DuelUtils.getArena(owner.getOwner()).get();
        Player player = PlayerUtils.getPlayer(owner.getOwner()).get();

        //Move card to cell right before the target enemy
        if(arena.getGrid().getDistanceBetweenCells(owner.getCurrentCell(), target) > 1){
            Row row = arena.getGrid().getRowBetweenCells(owner.getCurrentCell(), target);
            DuelUtils.moveCardToCell(row.getCells().get(row.getCells().size() - 2), player);
            CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(owner.getOwner()).get();
            cpp.stopMovingCard();

            arena.getGamemode().setTimePaused(true);


            ExcelsiorSponge.INSTANCE.getDirectionalAimArenaTimer().addDelayedTask(new AbstractTimer.DelayedTask(3) {
                @Override
                public void doTask() {
                    //battle
                    battle(arena, target, player);
                    arena.getGamemode().setTimePaused(false);
                }
            });
        } else {
            battle(arena, target, player);
        }
    }

    private void battle(Arena arena, Cell target, Player player){
        BattleResult battleResult = arena.getGamemode().battle(owner.getCurrentCell(), target);
        if(battleResult.getVictorCard().isPresent()){
            if(battleResult.getVictorCard().get() == owner){
                DuelUtils.moveCardToCell(target, player);
            }
        }
    }

    @Override
    public void drawCells(Player player) {
        for(Cell cell: getApplicableCells()){
            cell.drawCustom(player, CardMovementColors.ENEMY);
        }
    }
}
