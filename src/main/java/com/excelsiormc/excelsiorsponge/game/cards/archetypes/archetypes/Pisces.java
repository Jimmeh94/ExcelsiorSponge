package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Pisces extends Archetype {

    public Pisces() {
    }

    protected Pisces(Pisces a, CombatantProfile owner) {
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.RIVER, CellTerrains.OCEAN);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.DESERT, CellTerrains.LABYRINTH, CellTerrains.ARCTIC, CellTerrains.SCORCHED);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Pisces(this, owner);
    }
}
