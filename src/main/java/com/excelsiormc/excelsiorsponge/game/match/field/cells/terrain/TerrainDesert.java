package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainDesert extends CellTerrain {

    public TerrainDesert(GenerationPriority priority) {
        super(priority, BlockTypes.SANDSTONE);
    }

}
