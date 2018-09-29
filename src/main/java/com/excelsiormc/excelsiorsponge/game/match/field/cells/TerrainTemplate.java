package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.CellTerrainGradient;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.TerrainShapes;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TerrainTemplate {

    private List<CellTerrain> types;
    private Grid grid;
    private CellTerrainGradient baseGradient;

    public TerrainTemplate(Grid grid, CellTerrainGradient baseGradient) {
        types = new CopyOnWriteArrayList<>();
        this.grid = grid;
        this.baseGradient = baseGradient;
    }

    public Grid getGrid() {
        return grid;
    }

    public void addType(CellTerrain type){
        types.add(type);
    }

    public boolean hasType(CellTerrain cellType) {
        return types.contains(cellType);
    }

    public void generateTerrain(){

        /**
         * First generate the high priority terrains,
         * then medium,
         * then all but 1 low priority.
         * Fill all empty cells with that last low priority
         */

        List<CellTerrain> temp = getPriority(CellTerrain.GenerationPriority.HIGHEST);
        for(CellTerrain c: temp){
            c.generateTerrain(grid, TerrainShapes.getRandomShape(), TerrainFilters.getRandomFilter());
        }

        temp = getPriority(CellTerrain.GenerationPriority.MEDIUM);
        for(CellTerrain c: temp){
            c.generateTerrain(grid, TerrainShapes.getRandomShape(), TerrainFilters.getRandomFilter());
        }

        temp = getPriority(CellTerrain.GenerationPriority.LOW);
        for(CellTerrain c: temp){
            c.generateTerrain(grid, TerrainShapes.getRandomShape(), TerrainFilters.getRandomFilter());
        }

        System.out.println(baseGradient.getTypes().toString());

        for(Row row: grid.getRows()){
            for(Cell cell: row.getCells()){
                if(cell.getCellType() == null) {
                    cell.setCellType(TerrainTypes.getTerrainTypesFromTerrain(baseGradient.getType()));
                }
            }
        }
    }

    private List<CellTerrain> getPriority(CellTerrain.GenerationPriority priority){
        List<CellTerrain> give = new ArrayList<>();

        for(CellTerrain c: types){
            if(c.getPriority() == priority){
                give.add(c);
            }
        }
        return give;
    }
}
