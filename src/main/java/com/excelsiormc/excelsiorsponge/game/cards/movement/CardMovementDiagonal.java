package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.flowpowered.math.vector.Vector3d;

import java.util.List;

public class CardMovementDiagonal extends CardMovementNormal {

    public CardMovementDiagonal(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        Vector3d start = owner.getCurrentCell().getCenter();
        Grid grid = ExcelsiorSponge.INSTANCE.getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        return grid.getSquareGroupofCells(start, distanceInCells);
    }
}
