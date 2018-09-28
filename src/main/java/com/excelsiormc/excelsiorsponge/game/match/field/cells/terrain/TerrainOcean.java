package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainOcean extends CellTerrain {

    public TerrainOcean(GenerationPriority priority) {
        super(priority, BlockTypes.LAPIS_BLOCK);
    }

    @Override
    public void generateTerrain(Grid grid) {

    }
}
