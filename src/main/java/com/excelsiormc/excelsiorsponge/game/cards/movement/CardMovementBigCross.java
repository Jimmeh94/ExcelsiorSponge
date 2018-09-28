package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

public class CardMovementBigCross extends CardMovement {

    public CardMovementBigCross() {
        super(0);
    }

    @Override
    public List<MovementEntry> getAvailableSpaces() {
        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        List<Cell> temp = grid.getAvailableCellsCross(owner.getCurrentCell(), grid.getRowCount(), 2, grid.getRowLength(),
                2, true, PlayerUtils.getTeam(owner.getOwner()));
        List<MovementEntry> give = new ArrayList<>();
        for(Cell cell: temp){
            if(cell.isAvailable()){
                give.add(new MovementEntry(cell, MovementEntry.MovementEntryType.EMPTY));
            } else {
                give.add(new MovementEntry(cell, MovementEntry.MovementEntryType.ENEMY_OCCUPIED));
            }
        }
        return give;
    }
}
