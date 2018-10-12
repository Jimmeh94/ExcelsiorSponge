package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Capricorn extends Archetype {

    public Capricorn() {
    }

    protected Capricorn(Capricorn a, CombatantProfile owner) {
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.MOUNTAINS, CellTerrains.PLAINS, CellTerrains.FOREST, CellTerrains.RIVER);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.OCEAN, CellTerrains.ARCTIC, CellTerrains.LABYRINTH, CellTerrains.SCORCHED);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Capricorn(this, owner);
    }
}
