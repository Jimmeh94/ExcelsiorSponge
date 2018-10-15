package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;

import java.util.UUID;

public abstract class CardBaseMonster extends CardBase {

    public CardBaseMonster(UUID owner, CardDescriptor description, StatPower power, StatHealth health, CardMovement cardMovement, SummonType summonType) {
        super(owner, description, power, health, cardMovement, summonType);
    }
}
