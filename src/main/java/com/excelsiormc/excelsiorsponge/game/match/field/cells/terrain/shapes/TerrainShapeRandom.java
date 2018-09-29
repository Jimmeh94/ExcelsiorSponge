package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class TerrainShapeRandom implements TerrainShape {

    private int amount;

    public TerrainShapeRandom(int amount) {
        this.amount = amount;
    }

    @Override
    public List<Cell> calculateShape(Grid grid, CellTerrain terrain) {
        List<Cell> temp = grid.getAllCells();
        List<Cell> give = new CopyOnWriteArrayList<>();
        Random random = new Random();

        for(int i = 0; i < amount; i++){
            Cell cell = temp.get(random.nextInt(temp.size()));
            if(!give.contains(cell)) {
                give.add(cell);
            }
        }
        return give;
    }
}
