package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import org.spongepowered.api.block.BlockType;

public abstract class CellTerrain {

    protected GenerationPriority priority;
    protected BlockType terrainMat;

    public CellTerrain(GenerationPriority priority, BlockType terrainMat) {
        this.priority = priority;
        this.terrainMat = terrainMat;
    }

    public BlockType getTerrainMat() {
        return terrainMat;
    }

    public GenerationPriority getPriority() {
        return priority;
    }

    public abstract void generateTerrain(Grid grid);

    public enum GenerationPriority {
        HIGHEST,
        MEDIUM,
        LOW
    }

}
