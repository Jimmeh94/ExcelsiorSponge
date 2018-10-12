package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Aquarius extends Archetype {

    public Aquarius() {
    }

    protected Aquarius(Aquarius a, CombatantProfile owner) {
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.OCEAN, CellTerrains.RIVER, CellTerrains.MOUNTAINS);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.DESERT, CellTerrains.LABYRINTH, CellTerrains.ARCTIC, CellTerrains.SCORCHED);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Aquarius(this, owner);
    }
}
