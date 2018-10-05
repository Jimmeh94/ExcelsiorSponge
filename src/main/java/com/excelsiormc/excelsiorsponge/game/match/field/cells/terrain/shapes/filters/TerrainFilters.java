package com.excelsiormc.excelsiorsponge.game.match.field.cells.terrain.shapes.filters;

import java.util.Optional;
import java.util.Random;

public class TerrainFilters {

    public static Optional<TerrainFilter> getRandomFilter(){
        Random random = new Random();
        return Optional.of(new TerrainFilterRemoveRandom(random.nextInt(10) + 1));

        /*
        switch (random.nextInt(2)){
            case 0: return Optional.empty();
            case 1: return Optional.of(new TerrainFilterRemoveRandom(random.nextInt(5) + 1));
        }
        return Optional.empty();*/
    }

}
