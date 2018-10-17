package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.CellTerrainGradient;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.TerrainShapes;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilters;

import java.util.*;
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

        List<CellTerrain> temp = getPriority(CellTerrain.GenerationPriority.LOW);
        for(CellTerrain c: temp){
            c.generateTerrain(grid, TerrainShapes.getRandomShape(), TerrainFilters.getRandomFilter());
        }

        temp = getPriority(CellTerrain.GenerationPriority.MEDIUM);
        for(CellTerrain c: temp){
            c.generateTerrain(grid, TerrainShapes.getRandomShape(), TerrainFilters.getRandomFilter());
        }

        temp = getPriority(CellTerrain.GenerationPriority.HIGHEST);
        for(CellTerrain c: temp){
            c.generateTerrain(grid, TerrainShapes.getRandomShape(), TerrainFilters.getRandomFilter());
        }

        List<Cell> leftOvers = grid.getCellsWithoutType();
        int startSize = leftOvers.size();
        Set<Map.Entry<CellTerrains, Integer>> entries = baseGradient.getTypes().entrySet();
        Random random = new Random();

        int amount;

        if(leftOvers.size() > 0) {
            for (Map.Entry<CellTerrains, Integer> entry : entries) {
                double whole = entry.getValue();
                double decimal = (whole / 100);
                amount = (int) (startSize * decimal);
                if (amount < 1) {
                    amount = 1;
                }

                for (int i = 0; i < amount; i++) {
                    if(leftOvers.size() > 0) {
                        int index = random.nextInt(leftOvers.size());
                        leftOvers.get(index).setCellType(entry.getKey());
                        leftOvers.remove(index);
                    }
                }
            }

            for (Cell cell : leftOvers) {
                cell.setCellType(CellTerrains.getRandomTypeOfPriority(CellTerrain.GenerationPriority.LOW));
                leftOvers.remove(cell);
            }
        }
    }

    private List<CellTerrain> getPriority(CellTerrain.GenerationPriority priority){
        List<CellTerrain> give = new CopyOnWriteArrayList<>();

        for(CellTerrain c: types){
            if(c.getPriority() == priority){
                give.add(c);
            }
        }
        return give;
    }
}
