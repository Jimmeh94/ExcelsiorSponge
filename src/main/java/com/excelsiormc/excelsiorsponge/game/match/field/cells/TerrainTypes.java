package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.*;

import java.util.Random;

public enum TerrainTypes {

    FOREST(new TerrainForest(CellTerrain.GenerationPriority.LOW)),
    OCEAN(new TerrainOcean(CellTerrain.GenerationPriority.LOW)),
    CITY(new TerrainCity(CellTerrain.GenerationPriority.LOW)),
    DESERT(new TerrainDesert(CellTerrain.GenerationPriority.LOW)),
    PLAINS(new TerrainPlains(CellTerrain.GenerationPriority.LOW)),
    RIVER(new TerrainRiver(CellTerrain.GenerationPriority.MEDIUM)),
    ARTIC(new TerrainArtic(CellTerrain.GenerationPriority.MEDIUM)),
    MOUNTAINS(new TerrainMountains(CellTerrain.GenerationPriority.HIGHEST)),
    LABYRINTH(new TerrainLabyrinth(CellTerrain.GenerationPriority.HIGHEST));

    private CellTerrain cellType;
    private static final Random random = new Random();

    TerrainTypes(CellTerrain cellType) {
        this.cellType = cellType;
    }

    public CellTerrain getCellType() {
        return cellType;
    }

    public static TerrainTypes getRandomType(){
        return TerrainTypes.values()[random.nextInt(TerrainTypes.values().length)];
    }

    @Override
    public String toString(){
        String give = super.toString().toLowerCase();
        return give.substring(0, 1).toUpperCase() + give.substring(1);
    }

    public static TerrainTemplate getNewTemplate(Grid grid){
        TerrainTemplate template = new TerrainTemplate(grid);
        template.addType(PLAINS.getCellType());
        template.addType(DESERT.getCellType());

        int count = 2;
        while(count < 4){
            TerrainTypes temp = getRandomType();
            if(!template.hasType(temp.getCellType())){
                template.addType(temp.getCellType());
                count++;
            }
        }

        return template;
    }

    public static TerrainTypes getTerrainTypesFromTerrain(CellTerrain c){
        for(TerrainTypes type: TerrainTypes.values()){
            if(type.getCellType() == c){
                return type;
            }
        }
        return null;
    }
}
