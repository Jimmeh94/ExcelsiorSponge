package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainRiver extends CellTerrain {

    public TerrainRiver(GenerationPriority priority) {
        super(priority, BlockTypes.LAPIS_ORE);
    }

}
