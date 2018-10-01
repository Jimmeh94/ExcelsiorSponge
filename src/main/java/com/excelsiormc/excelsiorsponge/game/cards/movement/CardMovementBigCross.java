package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.MovementFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;

import java.util.List;

public class CardMovementBigCross extends CardMovement {

    public CardMovementBigCross(int distance, MovementFilter... filters) {
        super(distance, filters);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        return grid.getCellsCross(owner.getCurrentCell(), distanceInCells, distanceInCells, distanceInCells, distanceInCells);
    }
}
