package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Taurus extends Archetype {

    public Taurus() {
    }

    protected Taurus(Taurus a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.PLAINS, CellTerrains.FOREST, CellTerrains.MOUNTAINS);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.LABYRINTH, CellTerrains.SCORCHED, CellTerrains.ARCTIC);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Taurus(this, owner);
    }
}
