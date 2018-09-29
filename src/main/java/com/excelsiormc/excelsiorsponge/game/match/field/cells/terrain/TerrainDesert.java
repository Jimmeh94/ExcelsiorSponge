package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTypes;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.TerrainShape;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters.TerrainFilter;
import org.spongepowered.api.block.BlockTypes;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TerrainDesert extends CellTerrain {

    public TerrainDesert(GenerationPriority priority) {
        super(priority, BlockTypes.SANDSTONE);
    }

}
