package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;

import java.util.UUID;

public abstract class CardBaseTrap extends CardBase {

    public CardBaseTrap(UUID owner, CardDescriptor descriptor, CardMovement cardMovement, SummonType summonType) {
        super(owner, descriptor, new StatPower(0, 0), new StatHealth(0, 0),  cardMovement, summonType);
    }
}
