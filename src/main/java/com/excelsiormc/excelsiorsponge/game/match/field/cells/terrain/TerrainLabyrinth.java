package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainLabyrinth extends CellTerrain {

    public TerrainLabyrinth(GenerationPriority priority) {
        super(priority, BlockTypes.BEDROCK);
    }

    @Override
    public void generateTerrain(Grid grid) {

    }
}
