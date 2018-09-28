package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;

/**
 * A GridNormal contains a playing field of 11 x 11 cells, each cell separated by a 1 block wide divider
 * Generates rows starting from z -> z + distance
 */
public class GridNormal extends Grid {

    public GridNormal(Vector3d startingPos, String world, int rowCount, int rowLength, int cell, boolean drawGrid) {
        super(startingPos, world, rowCount, rowLength, cell, drawGrid, BlockTypes.OBSIDIAN, BlockTypes.STONE);
    }

    @Override
    protected void GenerateCells(Vector3d startingPos) {

        EditableVector use = new EditableVector(startingPos.clone());
        //use.subtract(1, 0, 1);

        EditableVector start = use.clone();
        for(int i = 0; i < rowCount; i++){
            Row row = new Row();
            for(int j = 0; j < rowLength; j++){
                row.addCell(new Cell(use.toVector3d(), cellDem, world, cellMat));
                use.setZ(use.getZ() + cellDem + 1);
            }
            use.setZ(start.getZ());
            use.setX(use.getX() + cellDem + 1);

            rows.add(row);
        }

        Row row = rows.get(rows.size() - 1);
        EditableVector end = new EditableVector(row.getCells().get(row.getCells().size() - 1).getCenter());
        end.add(cellDem, -1, cellDem);

        use = new EditableVector(startingPos.toInt().clone());
        use.subtract(1, 1, 1);

        World world = Sponge.getServer().getWorld(getWorld()).get();
        List<Location> temp = LocationUtils.getAllLocationsBetween(new Location(world, use.toVector3d()), new Location(world, end.toVector3d()));
        for(Location location: temp){
            use = new EditableVector(location.getPosition());
            use.add(0, 1, 0);
            boolean needToPaint = true;
            for(Row r: rows){
                for(Cell cell: r.getCells()){
                    if(needToPaint == true && cell.isWithinCell(use.toVector3d().toInt())){
                        needToPaint = false;
                    }
                }
            }
            if(needToPaint){
                //So we just made sure that the block above this one isn't in a cell, in other words,
                //This is a border between some cells
                use.subtract(0, 1, 0);
                border.add(use.toVector3d());
            }
        }
    }
}
