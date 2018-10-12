package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Leo extends Archetype {

    public Leo() {
    }

    protected Leo(Leo a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.PLAINS, CellTerrains.MOUNTAINS, CellTerrains.FOREST, CellTerrains.SCORCHED);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.OCEAN, CellTerrains.RIVER, CellTerrains.LABYRINTH, CellTerrains.ARCTIC);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Leo(this, owner);
    }
}
