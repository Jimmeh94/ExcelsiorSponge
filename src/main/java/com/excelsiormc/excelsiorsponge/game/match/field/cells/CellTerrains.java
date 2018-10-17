package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public enum CellTerrains {

    FOREST(new TerrainForest(CellTerrain.GenerationPriority.LOW)),
    OCEAN(new TerrainOcean(CellTerrain.GenerationPriority.LOW)),
    DESERT(new TerrainDesert(CellTerrain.GenerationPriority.LOW)),
    PLAINS(new TerrainPlains(CellTerrain.GenerationPriority.LOW)),

    CITY(new TerrainCity(CellTerrain.GenerationPriority.MEDIUM)),
    RIVER(new TerrainRiver(CellTerrain.GenerationPriority.MEDIUM)),
    ARCTIC(new TerrainArctic(CellTerrain.GenerationPriority.MEDIUM)),
    SCORCHED(new TerrainScorched(CellTerrain.GenerationPriority.MEDIUM)),

    MOUNTAINS(new TerrainMountains(CellTerrain.GenerationPriority.HIGHEST)),
    LABYRINTH(new TerrainLabyrinth(CellTerrain.GenerationPriority.HIGHEST));

    private CellTerrain cellType;
    private static final Random random = new Random();

    CellTerrains(CellTerrain cellType) {
        this.cellType = cellType;
    }

    public static CellTerrains getRandomTypeOfPriority(CellTerrain.GenerationPriority priority) {
        CellTerrains t = CellTerrains.values()[random.nextInt(CellTerrains.values().length)];

        while(t.getCellType().getPriority() != priority){
            t = CellTerrains.values()[random.nextInt(CellTerrains.values().length)];
        }

        return t;
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
        int max = 3 + random.nextInt(3);

        while(count < max){

            CellTerrains temp = getRandomType();
            if(temp.getCellType().getPriority() != CellTerrain.GenerationPriority.LOW && !template.hasType(temp.getCellType())){
                template.addType(temp.getCellType());
                count++;
            }
        }

        return template;
    }

    private static CellTerrainGradient getNewBaseGradient() {
        List<CellTerrains> temp = getTypesOf(CellTerrain.GenerationPriority.LOW);
        Random random = new Random();
        int amount = 4;

        CellTerrainGradient gradient = new CellTerrainGradient();

        int total = 100;
        double percentage;

        for(int i = 0; i < amount; i++){
            CellTerrains type = temp.get(random.nextInt(temp.size()));
            temp.remove(type);

            if(i < amount - 1){
                double whole = ((random.nextInt(100) + 1));
                percentage = total * (whole / 100);
                if(percentage < 1){
                    percentage = 1;
                }

                total -= percentage;
                gradient.addType(type, (int) percentage);
            } else {
                gradient.addType(type, total < 1 ? 1 : total);
            }
        }

        return gradient;
    }

    private static List<CellTerrains> getTypesOf(CellTerrain.GenerationPriority priority){
        List<CellTerrains> temp = new CopyOnWriteArrayList<>();
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
