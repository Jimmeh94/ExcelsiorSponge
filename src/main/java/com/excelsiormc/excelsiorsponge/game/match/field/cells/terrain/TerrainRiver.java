package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTypes;
import org.spongepowered.api.block.BlockTypes;

public class TerrainRiver extends CellTerrain {

    public TerrainRiver(GenerationPriority priority) {
        super(priority, BlockTypes.LAPIS_ORE);
    }

    @Override
    public void generateTerrain(Grid grid) {
        Cell start = grid.getRandomCell();

        Row row = grid.getHorizontalRow(start).get();
        for(Cell cell: row.getCells()){
            if(cell.getCellType() == null){
                cell.setCellType(TerrainTypes.getTerrainTypesFromTerrain(this));
            }
        }
    }
}
