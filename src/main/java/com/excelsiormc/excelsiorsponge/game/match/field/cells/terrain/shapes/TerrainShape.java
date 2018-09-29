package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;

import java.util.List;

public interface TerrainShape {

    List<Cell> calculateShape(Grid grid, CellTerrain terrain);

}
