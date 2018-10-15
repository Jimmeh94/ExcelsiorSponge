package com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonTypeEnergy;

import java.util.UUID;

public class CardAncientTreeofEnlightenment extends CardBaseMonster {

    public CardAncientTreeofEnlightenment(UUID owner, CardDescriptor descriptor) {
        super(owner, descriptor, new StatPower(600), new StatHealth(1500),
                new CardMovementNormal(1, new FilterIncludeEnemyEmptyCell()),
                new SummonTypeEnergy(3));
    }
}
