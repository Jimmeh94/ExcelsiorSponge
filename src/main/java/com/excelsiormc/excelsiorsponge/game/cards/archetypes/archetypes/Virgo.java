package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Virgo extends Archetype {

    public Virgo() {
    }

    protected Virgo(Virgo a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {
        addAdvantageousTerrain(CellTerrains.MOUNTAINS, CellTerrains.PLAINS, CellTerrains.DESERT, CellTerrains.FOREST);
    }

    @Override
    protected void setHinderingTerrain() {
        addHinderingTerrain(CellTerrains.OCEAN, CellTerrains.RIVER, CellTerrains.SCORCHED, CellTerrains.LABYRINTH);
    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Virgo(this, owner);
    }
}
