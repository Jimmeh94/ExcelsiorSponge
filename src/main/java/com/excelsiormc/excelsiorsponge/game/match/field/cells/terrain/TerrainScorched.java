package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainScorched extends CellTerrain {

    public TerrainScorched(GenerationPriority priority) {
        super(priority, BlockTypes.MAGMA);
    }
}
