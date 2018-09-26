package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
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
    public List<Cell> getAvailableSpaces() {
        List<Cell> cells = new ArrayList<>();

        Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        Cell current = owner.getCurrentCell();

        if(current != null){
            Optional<Cell> target;

            for(int i = 1; i <= distanceInCells; i++){
                target = grid.getCellInDirection(current, new Vector3d(i, 0, 0));
                if(target.isPresent()){
                    cells.add(target.get());
                }

                target = grid.getCellInDirection(current, new Vector3d(-i, 0, 0));
                if(target.isPresent()){
                    cells.add(target.get());
                }

                target = grid.getCellInDirection(current, new Vector3d(0, 0, i));
                if(target.isPresent()){
                    cells.add(target.get());
                }

                target = grid.getCellInDirection(current, new Vector3d(0, 0, -i));
                if(target.isPresent()){
                    cells.add(target.get());
                }
            }


        }

        return cells;
    }
}
