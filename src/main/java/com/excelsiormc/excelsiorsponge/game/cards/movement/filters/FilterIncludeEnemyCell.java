package com.excelsiormc.excelsiorsponge.game.cards.movement.filters;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementColors;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
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
        ExcelsiorSponge.INSTANCE.getMatchMaker()
                .getArenaManager().findArenaWithCombatant(owner.getOwner()).get()
                .getGamemode().battle(owner.getCurrentCell(), target);
    }

    @Override
    public void drawCells(Player player) {
        for(Cell cell: getApplicableCells()){
            cell.drawCustom(player, CardMovementColors.ENEMY);
        }
    }
}
