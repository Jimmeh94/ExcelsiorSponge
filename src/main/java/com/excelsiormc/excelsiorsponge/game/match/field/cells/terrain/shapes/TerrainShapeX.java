package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;

import java.util.List;

public class TerrainShapeX implements TerrainShape {

    private int radius;

    public TerrainShapeX(int radius) {
        this.radius = radius;
    }

    @Override
    public List<Cell> calculateShape(Grid grid, CellTerrain terrain) {
        return grid.getXGroupOfCells(radius, DuelUtils.getCellWithoutTerrainOrOverrideable(terrain, grid));
    }
}
