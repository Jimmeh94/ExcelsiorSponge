package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;

import java.util.List;
import java.util.Random;

public class TerrainFilterRemoveRandom implements TerrainFilter {

    private int amount;

    public TerrainFilterRemoveRandom(int amount) {
        this.amount = amount;
    }

    @Override
    public List<Cell> applyFilter(List<Cell> cells, Grid grid, CellTerrain terrain) {

        if(cells.size() == 0){
            return cells;
        }

        Random random = new Random();

        if(cells.size() < amount){
            amount = cells.size() / 2;
        }

        for(int i = 0; i < amount; i++){
            cells.remove(random.nextInt(cells.size()));
        }

        return cells;
    }
}
