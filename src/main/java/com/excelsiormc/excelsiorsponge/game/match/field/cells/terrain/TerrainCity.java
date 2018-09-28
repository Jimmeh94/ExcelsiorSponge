package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainCity extends CellTerrain {

    public TerrainCity(GenerationPriority priority) {
        super(priority, BlockTypes.CONCRETE);
    }

    @Override
    public void generateTerrain(Grid grid) {

    }
}
