package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainArtic extends CellTerrain {

    public TerrainArtic(GenerationPriority priority) {
        super(priority, BlockTypes.SNOW);
    }

    @Override
    public void generateTerrain(Grid grid) {

    }
}
