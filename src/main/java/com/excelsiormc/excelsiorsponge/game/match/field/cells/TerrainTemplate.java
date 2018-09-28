package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TerrainTemplate {

    private CellTerrain base;
    private List<CellTerrain> types;
    private Grid grid;

    public TerrainTemplate(Grid grid) {
        types = new CopyOnWriteArrayList<>();
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setBase(CellTerrain base) {
        this.base = base;
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
            c.generateTerrain(grid);
        }

        temp = getPriority(CellTerrain.GenerationPriority.MEDIUM);
        for(CellTerrain c: temp){
            c.generateTerrain(grid);
        }

        temp = getPriority(CellTerrain.GenerationPriority.LOW);
        for(CellTerrain c: temp){
            c.generateTerrain(grid);
        }

        for(Row row: grid.getRows()){
            for(Cell cell: row.getCells()){
                if(cell.getCellType() == null) {
                    cell.setCellType(TerrainTypes.getTerrainTypesFromTerrain(base));
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
