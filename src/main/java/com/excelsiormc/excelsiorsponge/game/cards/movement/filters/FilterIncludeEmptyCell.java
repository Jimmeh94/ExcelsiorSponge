package com.excelsiormc.excelsiorsponge.game.cards.movement.filters;

import com.excelsiormc.excelsiorsponge.utils.BlockStateColors;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class FilterIncludeEmptyCell extends MovementFilter {

    @Override
    public void filter(List<Cell> cells) {
        for(Cell cell: cells){
            if(cell.isAvailable()){
                addCell(cell);
            }
        }
    }

    @Override
    public void action(Cell target) {
        DuelUtils.moveCardToCell(PlayerUtils.getPlayer(owner.getOwner()).get());
    }

    @Override
    public void drawCells(Player player) {
        for(Cell cell: getApplicableCells()){
            cell.drawCustom(player, BlockStateColors.EMPTY);
        }
    }
}
