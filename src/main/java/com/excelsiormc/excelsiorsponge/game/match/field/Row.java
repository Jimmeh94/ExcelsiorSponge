package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A row is a row of cells generated from z -> z + distance
 */
public class Row {

    private List<Cell> cells;

    public Row(){
        cells = new ArrayList<>();
    }

    public void addCell(Cell cell){
        if(!cells.contains(cell)){
            cells.add(cell);
        }
    }

    public boolean containsCell(Cell cell){
        for(Cell c: cells){
            if(c == cell){
                return true;
            }
        }
        return false;
    }

    public void drawCells() {
        for(Cell cell: cells){
            cell.drawCell();
        }
    }

    public boolean isWithinCell(Vector3i check) {
        for(Cell cell: cells){
            if(cell.isWithinCell(check)){
                return true;
            }
        }
        return false;
    }

    public Optional<Cell> getCell(Vector3i check) {
        for(Cell cell: cells){
            if(cell.isWithinCell(check)){
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }

    public void resetCells() {
        for(Cell cell: cells){
            cell.setAvailable(true);
        }
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Cell getCenterCell() {
        int index = cells.size() / 2;
        if(index % 2 == 0){
            index++;
        }
        return cells.get(index);
    }

    public Vector3d getCenterPos(){
        return LocationUtils.getMiddleLocation(cells.get(0).getCenter(), cells.get(cells.size() - 1).getCenter());
    }
}
