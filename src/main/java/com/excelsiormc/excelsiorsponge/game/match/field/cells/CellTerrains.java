package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum CellTerrains {

    FOREST(new TerrainForest(CellTerrain.GenerationPriority.LOW)),
    OCEAN(new TerrainOcean(CellTerrain.GenerationPriority.LOW)),
    CITY(new TerrainCity(CellTerrain.GenerationPriority.LOW)),
    DESERT(new TerrainDesert(CellTerrain.GenerationPriority.LOW)),
    PLAINS(new TerrainPlains(CellTerrain.GenerationPriority.LOW)),
    RIVER(new TerrainRiver(CellTerrain.GenerationPriority.MEDIUM)),
    ARCTIC(new TerrainArctic(CellTerrain.GenerationPriority.MEDIUM)),
    MOUNTAINS(new TerrainMountains(CellTerrain.GenerationPriority.HIGHEST)),
    LABYRINTH(new TerrainLabyrinth(CellTerrain.GenerationPriority.HIGHEST));

    private CellTerrain cellType;
    private static final Random random = new Random();

    CellTerrains(CellTerrain cellType) {
        this.cellType = cellType;
    }

    public CellTerrain getCellType() {
        return cellType;
    }

    public static CellTerrains getRandomType(){
        return CellTerrains.values()[random.nextInt(CellTerrains.values().length)];
    }

    @Override
    public String toString(){
        String give = super.toString().toLowerCase();
        return give.substring(0, 1).toUpperCase() + give.substring(1);
    }

    public static TerrainTemplate getNewTemplate(Grid grid){
        TerrainTemplate template = new TerrainTemplate(grid, getNewBaseGradient());

        int count = 0;
        while(count < random.nextInt(3) + 4){
            CellTerrains temp = getRandomType();
            if(!template.hasType(temp.getCellType())){
                template.addType(temp.getCellType());
                count++;
            }
        }

        return template;
    }

    private static CellTerrainGradient getNewBaseGradient() {
        List<CellTerrains> temp = getTypesOf(CellTerrain.GenerationPriority.LOW);
        Random random = new Random();
        int amount = random.nextInt(temp.size());
        if(amount == 0){
            amount++;
        }
        CellTerrainGradient gradient = new CellTerrainGradient();

        for(int i = 0; i < amount; i++){
            gradient.addType(temp.get(random.nextInt(temp.size())));
        }

        return gradient;
    }

    private static List<CellTerrains> getTypesOf(CellTerrain.GenerationPriority priority){
        List<CellTerrains> temp = new ArrayList<>();
        for(CellTerrains t: CellTerrains.values()){
            if(t.getCellType().getPriority() == priority){
                temp.add(t);
            }
        }
        return temp;
    }

    private static CellTerrains getRandomTypeOf(CellTerrain.GenerationPriority priority){
        List<CellTerrains> temp = getTypesOf(priority);
        return temp.get(random.nextInt(temp.size() - 1));
    }

    public static CellTerrains getTerrainTypesFromTerrain(CellTerrain c){
        for(CellTerrains type: CellTerrains.values()){
            if(type.getCellType() == c){
                return type;
            }
        }
        return null;
    }

    public boolean canBeOverriddenBy(CellTerrain terrain) {
        switch(this.getCellType().getPriority()){
            case HIGHEST: return false;
            case MEDIUM: return terrain.getPriority() == CellTerrain.GenerationPriority.HIGHEST ? true : false;
            case LOW: return terrain.getPriority() != CellTerrain.GenerationPriority.LOW ? true : false;
        }
        return false;
    }
}
