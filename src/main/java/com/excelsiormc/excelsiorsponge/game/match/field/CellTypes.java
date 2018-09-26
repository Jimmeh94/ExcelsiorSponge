package com.excelsiormc.excelsiorsponge.game.match.field;

import java.util.Random;

public enum CellTypes {

    FOREST(new CellType.Forest()),
    RIVER(new CellType.River()),
    OCEAN(new CellType.Ocean()),
    CITY(new CellType.City()),
    DESERT(new CellType.Desert()),
    PLAINS(new CellType.Plains()),
    ARTIC(new CellType.Artic()),
    MOUNTAINS(new CellType.Mountains());

    private CellType cellType;
    private static final Random random = new Random();

    CellTypes(CellType cellType) {
        this.cellType = cellType;
    }

    public CellType getCellType() {
        return cellType;
    }

    public static CellTypes getRandomType(){
        return CellTypes.values()[random.nextInt(CellTypes.values().length)];
    }

    @Override
    public String toString(){
        String give = super.toString().toLowerCase();
        return give.substring(0, 1).toUpperCase() + give.substring(1);
    }
}
