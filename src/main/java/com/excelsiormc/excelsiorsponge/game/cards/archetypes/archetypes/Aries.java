package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Aries extends Archetype {

    public Aries() {
    }

    protected Aries(Aries a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.MOUNTAINS, CellTerrains.SCORCHED, CellTerrains.DESERT);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.OCEAN, CellTerrains.ARCTIC, CellTerrains.LABYRINTH, CellTerrains.RIVER);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Aries(this, owner);
    }
}
