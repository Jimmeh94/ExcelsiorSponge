package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;

import java.util.HashMap;
import java.util.Map;

public class CellTerrainGradient {

    private Map<CellTerrains, Integer> types;

    public CellTerrainGradient(){this.types = new HashMap<>();}

    public void addType(CellTerrains type, int percentage){
        types.put(type, percentage);
    }

    public Map<CellTerrains, Integer> getTypes() {
        return types;
    }
}
