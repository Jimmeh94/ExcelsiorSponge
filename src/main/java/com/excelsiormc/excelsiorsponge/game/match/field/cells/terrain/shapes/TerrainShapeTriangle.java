package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.util.Direction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class TerrainShapeTriangle implements TerrainShape {

    private int baseRadius;
    private Vector3i direction;

    public TerrainShapeTriangle(int baseRadius, Vector3i direction) {
        this.baseRadius = baseRadius;
        this.direction = direction;
    }

    @Override
    public List<Cell> calculateShape(Grid grid, CellTerrain terrain) {
        Cell start = DuelUtils.getCellWithoutTerrainOrOverrideable(terrain, grid);
        List<Cell> cells = new CopyOnWriteArrayList<>();

        int temp = baseRadius;
        while(temp >= 0){
            cells.addAll(grid.getRowInDirectionForLength(start, temp, LocationUtils.rotateDirectionBy90(direction)).getCells());
            cells.addAll(grid.getRowInDirectionForLength(start, temp, LocationUtils.rotateDirectionBy270(direction)).getCells());

            Optional<Cell> next = grid.getCellInDirection(start, direction.toDouble(), false);
            if(!next.isPresent()){
                temp = -1;
            } else {
                start = next.get();
            }

            temp--;
        }

        return cells;
    }
}
