package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;

import java.util.Random;

public class TerrainShapes {

    public static TerrainShape getRandomShape(){

        Random random = new Random();

        switch (random.nextInt(6)){
            case 0: return new TerrainShapeRow(random.nextInt(5) + 1, LocationUtils.getRandomDirection2D());
            case 1: return new TerrainShapeSquare(random.nextInt(3) + 1);
            case 2: return new TerrainShapeRandom(random.nextInt(15) + 3);
            case 3: return new TerrainShapeCross(random.nextInt(5) + 1);
            case 4: return new TerrainShapeX(random.nextInt(4) + 1);
            case 5: return new TerrainShapeTriangle(random.nextInt(3) + 1, LocationUtils.getRandomDirection2D());
        }

        return null;
    }

}
