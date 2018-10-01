package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.MovementFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;

import java.util.List;

public class CardMovementSquare extends CardMovement{

    public CardMovementSquare(int distanceInCells, MovementFilter... filters) {
        super(distanceInCells, filters);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        Cell current = owner.getCurrentCell();

        return grid.getSquareGroupofCells(current, distanceInCells);
    }
}
