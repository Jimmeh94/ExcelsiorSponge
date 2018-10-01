package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.Row;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.flowpowered.math.vector.Vector3i;

import java.util.List;

public class TerrainShapeRow implements TerrainShape{

    private int rowLength;
    private Vector3i rowDirection;

    public TerrainShapeRow(int rowLength, Vector3i rowDirection){
        this.rowLength = rowLength;
        this.rowDirection = rowDirection;
    }

    @Override
    public List<Cell> calculateShape(Grid grid, CellTerrain terrain) {
        Cell start = DuelUtils.getCellWithoutTerrainOrOverrideable(terrain, grid);

        Row row = grid.getRowInDirectionForLength(start, rowLength, rowDirection);
        return row.getCells();
    }
}
