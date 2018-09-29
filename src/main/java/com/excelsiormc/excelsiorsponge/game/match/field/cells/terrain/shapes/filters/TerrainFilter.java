package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;

import java.util.List;

public interface TerrainFilter {

    List<Cell> applyFilter(List<Cell> cells, Grid grid, CellTerrain terrain);

}
