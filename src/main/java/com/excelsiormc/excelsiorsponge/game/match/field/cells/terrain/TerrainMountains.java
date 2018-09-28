package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import org.spongepowered.api.block.BlockTypes;

public class TerrainMountains extends CellTerrain {

    public TerrainMountains(GenerationPriority priority) {
        super(priority, BlockTypes.COAL_ORE);
    }

    @Override
    public void generateTerrain(Grid grid) {

    }
}
