package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Gemini extends Archetype {

    public Gemini() {
    }

    protected Gemini(Gemini a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.CITY, CellTerrains.PLAINS, CellTerrains.LABYRINTH);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.ARCTIC, CellTerrains.OCEAN, CellTerrains.MOUNTAINS, CellTerrains.SCORCHED);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Gemini(this, owner);
    }
}
