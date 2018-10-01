package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainArctic extends CellTerrain {

    public TerrainArctic(GenerationPriority priority) {
        super(priority, BlockTypes.SNOW);
    }

}
