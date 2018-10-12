package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Cancer extends Archetype {

    public Cancer() {
    }

    protected Cancer(Cancer a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.RIVER, CellTerrains.PLAINS, CellTerrains.OCEAN);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.CITY, CellTerrains.LABYRINTH, CellTerrains.SCORCHED, CellTerrains.DESERT, CellTerrains.ARCTIC);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Cancer(this, owner);
    }
}
