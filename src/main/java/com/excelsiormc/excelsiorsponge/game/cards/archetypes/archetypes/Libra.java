package com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.Archetype;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

public class Libra extends Archetype {

    public Libra() {
    }

    protected Libra(Libra a, CombatantProfile owner){
        super(a, owner);
    }

    @Override
    protected void setAdvantageousTerrain() {

    }

    @Override
    protected void setHinderingTerrain() {

    }

    @Override
    protected Archetype getNew(CombatantProfile owner) {
        return new Libra(this, owner);
    }
}
