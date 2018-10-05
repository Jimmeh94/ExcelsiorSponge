package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.TerrainShape;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilterRemoveRandom;
import org.spongepowered.api.block.BlockTypes;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TerrainLabyrinth extends CellTerrain {

    public TerrainLabyrinth(GenerationPriority priority) {
        super(priority, BlockTypes.BEDROCK);
    }

    @Override
    public void generateTerrain(Grid grid, TerrainShape shape, Optional<TerrainFilter> filter) {
        List<Cell> cells = shape.calculateShape(grid, this);

        //Need to make sure not in first or last 2 rows
        for(Cell cell: cells){
            int index = grid.getRows().indexOf(grid.getHorizontalRow(cell).get());
            if(index == 0 || index == 1 || index == grid.getRows().size() - 2 || index == grid.getRows().size() - 1){
                cells.remove(cell);
            }
        }

        Random random = new Random();
        (new TerrainFilterRemoveRandom(random.nextInt(cells.size() + 1))).applyFilter(cells, grid, this);

        setCells(cells);
    }

}
