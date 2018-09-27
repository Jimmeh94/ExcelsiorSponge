package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class CardMovementDiagonal extends CardMovementNormal {

    public CardMovementDiagonal(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<MovementEntry> getAvailableSpaces() {
        Vector3d start = owner.getCurrentCell().getCenter();
        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        List<Cell> temp = grid.getSquareGroupofCells(start, distanceInCells, true, PlayerUtils.getTeam(owner.getOwner()));
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
