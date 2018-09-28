package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.Sponge;

import java.util.List;

public class TerrainPlains extends CellTerrain {

    public TerrainPlains(GenerationPriority priority) {
        super(priority);
    }

    @Override
    public void generateTerrain(Grid grid) {
        Cell start = grid.getRandomCell();

        while(start.getCellType() != null){
            start = grid.getRandomCell();
        }

        List<Cell> cells = grid.getSquareGroupofCells(start.getCenter(), 3, false, null);
        for(Cell cell: cells){
            if(cell.getCellType() == null){
                cell.setCellType(this);
                cell.drawAvailableSpaceForPlayer(Sponge.getServer().getPlayer("__Jimmeh__").get());
            }
        }
    }
}
