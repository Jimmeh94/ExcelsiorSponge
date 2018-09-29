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
 * A GridNormal contains a playing field of cell x cell cells, each cell separated by a 1 block wide divider
 * Generates rows starting from z -> z + distance
 */
public class GridNormal extends Grid {

    //StartingPos will be where the corner of the border is
    public GridNormal(Vector3d startingPos, String world, int rowCount, int rowLength, int cell, boolean drawGrid) {
        super(startingPos, world, rowCount, rowLength, cell, drawGrid, BlockTypes.OBSIDIAN, BlockTypes.STONE);
    }

    @Override
    protected void generateCells() {
        EditableVector use = new EditableVector(startPos.toInt().clone());
        use.add(1, 1, 1);

        final EditableVector reference = use.clone();

        for(int i = 0; i < rowCount; i++){
            Row row = new Row();
            for(int j = 0; j < rowLength; j++){
                row.addCell(new Cell(use.toVector3d(), cellDim, world, cellMat));
                use.setZ(use.getZ() + cellDim + 1);
            }
            use.setZ(reference.getZ());
            use.setX(use.getX() + cellDim + 1);

            rows.add(row);
        }

        Row row = rows.get(rows.size() - 1);
        EditableVector end = new EditableVector(row.getCells().get(row.getCells().size() - 1).getCenter());
        end.add(cellDim  - 1, -1, cellDim - 1);

        use = reference.clone();
        use.subtract(1, 1, 1);

        World world = Sponge.getServer().getWorld(getWorld()).get();
        List<Location> temp = LocationUtils.getAllLocationsBetween(new Location(world, use.toVector3d()), new Location(world, end.toVector3d()));
        for(Location location: temp){
            if(!isCell(location.getPosition())){
                border.add(location.getPosition());
            }
        }
    }
}
