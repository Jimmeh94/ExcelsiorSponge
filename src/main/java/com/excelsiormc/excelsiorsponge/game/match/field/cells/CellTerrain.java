package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;

public abstract class CellTerrain {

    protected GenerationPriority priority;

    public CellTerrain(GenerationPriority priority) {
        this.priority = priority;
    }

    public GenerationPriority getPriority() {
        return priority;
    }

    public abstract void generateTerrain(Grid grid);

    public enum GenerationPriority {
        HIGHEST,
        MEDIUM,
        LOW
    }

}
