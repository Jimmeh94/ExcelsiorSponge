package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.TerrainShape;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilter;
import org.spongepowered.api.block.BlockType;

import java.util.List;
import java.util.Optional;

public abstract class CellTerrain {

    protected GenerationPriority priority;
    protected BlockType terrainMat;
    protected TerrainBuild build;

    public CellTerrain(GenerationPriority priority, BlockType terrainMat) {
        this.priority = priority;
        this.terrainMat = terrainMat;
    }

    public TerrainBuild getBuild() {
        return build;
    }

    public BlockType getTerrainMat() {
        return terrainMat;
    }

    public GenerationPriority getPriority() {
        return priority;
    }

    protected void setCells(List<Cell> cells){
        for(Cell cell: cells){
            if(cell.getCellType() == null){
                cell.setCellType(CellTerrains.getTerrainTypesFromTerrain(this));
            }
        }
    }

    public void generateTerrain(Grid grid, TerrainShape shape, Optional<TerrainFilter> filter) {
        List<Cell> cells = shape.calculateShape(grid, this);

        if(filter.isPresent()){
            filter.get().applyFilter(cells, grid, this);
        }

        setCells(cells);

        if(cells.size() > 0) {
            for (Cell cell : cells) {
                build.draw(cell, cell.getVector3ds().get(0).clone().add(0, 1, 0));
            }
        }
    }

    public void setBuild(TerrainBuild terrainBuild) {
        this.build = terrainBuild;
    }

    public enum GenerationPriority {
        HIGHEST,
        MEDIUM,
        LOW
    }

}
