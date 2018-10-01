package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class CellTerrainGradient {

    private List<CellTerrains> types;

    public CellTerrainGradient(CellTerrains... types){
        this.types = Arrays.asList(types);
    }

    public CellTerrainGradient(){this.types = new CopyOnWriteArrayList<>();
    }

    public void addType(CellTerrains type){
        types.add(type);
    }

    public List<CellTerrains> getTypes() {
        return types;
    }

    public CellTerrain getType() {
        Random random = new Random();
        return types.get(random.nextInt(types.size())).getCellType();
    }
}
