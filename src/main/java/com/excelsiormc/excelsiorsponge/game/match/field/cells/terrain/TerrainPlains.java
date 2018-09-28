package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;

import java.util.List;

public class TerrainPlains extends CellTerrain {

    public TerrainPlains(GenerationPriority priority) {
        super(priority, BlockTypes.GRASS);
    }

    @Override
    public void generateTerrain(Grid grid) {
        Cell start = grid.getRandomCell();

        /*while(start.getCellType() != null){
            start = grid.getRandomCell();
        }*/

        List<Cell> cells = grid.getSquareGroupofCells(start, 2, false, null);
        for(Cell cell: cells){
            if(cell.getCellType() == null){
                cell.setCellType(TerrainTypes.getTerrainTypesFromTerrain(this));
            }
        }
    }
}
