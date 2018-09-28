package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTypes;
import org.spongepowered.api.block.BlockTypes;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class TerrainLabyrinth extends CellTerrain {

    public TerrainLabyrinth(GenerationPriority priority) {
        super(priority, BlockTypes.BEDROCK);
    }

    @Override
    public void generateTerrain(Grid grid) {
        Cell start = grid.getRandomCell();

        while(start.getCellType() != null){
            start = grid.getRandomCell();
        }

        Random random = new Random();
        Row row = null;
        switch (random.nextInt(2)){
            case 0: row = grid.getHorizontalRow(start).get();
                break;
            case 1: row = grid.getVerticalRow(start).get();
        }

        row = row.clone();

        for(int i = 0; i < random.nextInt(5); i++){
            row.getCells().remove(random.nextInt(row.getCells().size()));
        }

        for(Cell cell: row.getCells()){
            if(cell.getCellType() == null){
                cell.setCellType(TerrainTypes.getTerrainTypesFromTerrain(this));
            }
        }

    }
}
