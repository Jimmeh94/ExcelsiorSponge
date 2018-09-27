package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Normal movement is x cells forward, back, left and right
 */
public class CardMovementNormal extends CardMovement {

    public CardMovementNormal(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<MovementEntry> getAvailableSpaces() {
        List<MovementEntry> cells = new ArrayList<>();

        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        Cell current = owner.getCurrentCell();

        if(current != null){
            List<Cell> temp = grid.getAdjacentAvailableCells(current, distanceInCells, true, PlayerUtils.getTeam(owner.getOwner()));
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
