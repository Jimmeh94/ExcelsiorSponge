package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Sagittarius extends Archetype {

    public Sagittarius() {
    }

    protected Sagittarius(Sagittarius a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.PLAINS, CellTerrains.DESERT, CellTerrains.SCORCHED);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.MOUNTAINS, CellTerrains.OCEAN, CellTerrains.LABYRINTH, CellTerrains.RIVER, CellTerrains.ARCTIC);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Sagittarius(this, owner);
    }
}
