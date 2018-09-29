package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.TerrainShape;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilter;
import org.spongepowered.api.block.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class CellTerrain {

    protected GenerationPriority priority;
    protected BlockType terrainMat;
    protected List<TerrainBuild> build;

    public CellTerrain(GenerationPriority priority, BlockType terrainMat) {
        this.priority = priority;
        this.terrainMat = terrainMat;
        build = new ArrayList<>();

        loadTerrainBuild();
    }

    protected abstract void loadTerrainBuild();

    public BlockType getTerrainMat() {
        return terrainMat;
    }

    public GenerationPriority getPriority() {
        return priority;
    }

    protected void setCells(List<Cell> cells){
        for(Cell cell: cells){
            if(cell.getCellType() == null){
                cell.setCellType(TerrainTypes.getTerrainTypesFromTerrain(this));
            }
        }
    }

    public void generateTerrain(Grid grid, TerrainShape shape, Optional<TerrainFilter> filter) {
        List<Cell> cells = shape.calculateShape(grid, this);

        if(filter.isPresent()){
            filter.get().applyFilter(cells, grid, this);
        }

        setCells(cells);

        Random random = new Random();
        for(Cell cell: cells){
            build.get(random.nextInt(build.size())).draw(cell.getCenter().clone().sub(1, -1, 1));
        }
    }

    public void addBuild(TerrainBuild terrainBuild) {
        this.build.add(terrainBuild);
    }

    public enum GenerationPriority {
        HIGHEST,
        MEDIUM,
        LOW
    }

}
