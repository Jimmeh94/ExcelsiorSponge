package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class CardMovementSquare extends CardMovement{

    public CardMovementSquare(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<MovementEntry> getAvailableSpaces() {
        List<MovementEntry> cells = new ArrayList<>();

        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        Cell current = owner.getCurrentCell();

        if(current != null){
            List<Cell> temp = grid.getSquareGroupofAvailableCells(current, distanceInCells, false, null);
            for(Cell cell: temp){
                if(cell.isAvailable()){
                    cells.add(new MovementEntry(cell, MovementEntry.MovementEntryType.EMPTY));
                } else {
                    cells.add(new MovementEntry(cell, MovementEntry.MovementEntryType.ENEMY_OCCUPIED));
                }
            }
        }

        return cells;
    }
}
