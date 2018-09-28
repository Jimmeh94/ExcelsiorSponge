package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainForest extends CellTerrain {

    public TerrainForest(GenerationPriority priority) {
        super(priority, BlockTypes.LEAVES);
    }

    @Override
    public void generateTerrain(Grid grid) {

    }
}
