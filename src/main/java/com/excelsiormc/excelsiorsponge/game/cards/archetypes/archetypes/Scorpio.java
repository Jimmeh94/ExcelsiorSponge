package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Scorpio extends Archetype {

    public Scorpio() {
    }

    protected Scorpio(Scorpio a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.OCEAN, CellTerrains.RIVER, CellTerrains.PLAINS, CellTerrains.CITY);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.LABYRINTH, CellTerrains.ARCTIC, CellTerrains.SCORCHED);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Scorpio(this, owner);
    }
}
