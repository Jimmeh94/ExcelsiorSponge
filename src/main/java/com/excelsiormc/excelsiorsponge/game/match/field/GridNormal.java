package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.utils.EditableVector;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockTypes;

/**
 * A GridNormal contains a playing field of 11 x 11 cells, each cell separated by a 1 block wide divider
 */
public class GridNormal extends Grid {

    public GridNormal(Vector3d startingPos, String world, int gridX, int gridZ, int cellX, int cellZ, boolean drawGrid) {
        super(startingPos, world, gridX, gridZ, cellX, cellZ, drawGrid, BlockTypes.OBSIDIAN, BlockTypes.STONE);
    }

    @Override
    protected void GenerateCells(Vector3d startingPos) {

        EditableVector use = new EditableVector(startingPos.clone());
        use.setY(use.getY() + 1);

        EditableVector endPosition = use.clone();
        EditableVector start = use.clone();
        for(int i = 0; i < gridDemX; i++){
            for(int j = 0; j < gridDemZ; j++){
                cells.add(new Cell(use.toVector3d(), cellDemX, cellDemZ, world, cellMat));
                use.setZ(use.getZ() + cellDemZ + 1);
                //Plus 4 with 3 wide cells will leave a 1 block open area between each cell
                if(j == 10){
                    endPosition = use.clone();
                    endPosition.setZ(endPosition.getZ());
                }
            }
            use.setZ(startingPos.getZ());

            if(i < gridDemX - 1) {
                use.setX(use.getX() + cellDemX + 1);
            } else {
                endPosition.setX(use.getX() + cellDemX + 1);
            }
        }

        start.setX(startingPos.getFloorX() - 1);
        start.setZ(startingPos.getFloorZ() - 1);
        start.setY(start.getY() - 1);
        endPosition.setY(endPosition.getY() - 1);

        use = start.clone();
        int sx = startingPos.getFloorX(), ex = (int) endPosition.getX();
        int sz = startingPos.getFloorZ(), ez = (int) endPosition.getZ();

        for(int i = 0; i < Math.max(sx, ex) - Math.min(sx, ex); i++){
            for(int j = 0; j < Math.max(sz, ez) - Math.min(sz, ez); j++){
                EditableVector temp = use.clone();
                temp.setY(temp.getY() + 1);

                boolean needToPaint = true;
                for(Cell cell: cells){
                    if(needToPaint == true && cell.isWithinCell(temp.toVector3d())){
                        needToPaint = false;
                    }
                }
                if(needToPaint){
                    //So we just made sure that the block above this one isn't in a cell, in other words,
                    //This is a border between some cells
                    border.add(use.toVector3d());
                }

                use.setZ(use.getZ() + 1);
            }

            use.setZ(startingPos.clone().sub(1, 0, 1).getFloorZ());
            use.setX(use.getX() + 1);
        }

    }
}
