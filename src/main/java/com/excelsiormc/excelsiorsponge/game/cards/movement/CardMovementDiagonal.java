package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.MovementFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.flowpowered.math.vector.Vector3i;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CardMovementDiagonal extends CardMovementNormal {

    public CardMovementDiagonal(int distanceInCells, MovementFilter... filters) {
        super(distanceInCells, filters);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        List<Cell> give = new CopyOnWriteArrayList<>();
        Cell start = owner.getCurrentCell();

        Row row = grid.getRowInDirectionForLength(start, distanceInCells, new Vector3i(1, 0, 1));
        row.getCells().remove(start);
        give.addAll(row.getCells());

        row = grid.getRowInDirectionForLength(start, distanceInCells, new Vector3i(1, 0, -1));
        row.getCells().remove(start);
        give.addAll(row.getCells());

        row = grid.getRowInDirectionForLength(start, distanceInCells, new Vector3i(-1, 0, 1));
        row.getCells().remove(start);
        give.addAll(row.getCells());

        row = grid.getRowInDirectionForLength(start, distanceInCells, new Vector3i(-1, 0, -1));
        row.getCells().remove(start);
        give.addAll(row.getCells());

        return give;
    }
}
