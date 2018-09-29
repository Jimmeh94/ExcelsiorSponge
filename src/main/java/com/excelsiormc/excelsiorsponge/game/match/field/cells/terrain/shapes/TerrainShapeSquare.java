package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;

import java.util.List;

public class TerrainShapeSquare implements TerrainShape {

    private int radius;

    public TerrainShapeSquare(int radius){
        this.radius = radius;
    }

    @Override
    public List<Cell> calculateShape(Grid grid, CellTerrain terrain) {
        Cell start = DuelUtils.getCellWithoutTerrainOrOverrideable(terrain, grid);

        return grid.getSquareGroupofCells(start, radius);
    }
}
