package com.excelsiormc.excelsiorsponge.game.cards.summon;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;

public abstract class SummonType {

    protected CardBase owner;

    public abstract boolean canSummon();

    public abstract void summon();

    public void setOwner(CardBase owner) {
        this.owner = owner;
    }
}
