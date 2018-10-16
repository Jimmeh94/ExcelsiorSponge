package com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementSquare;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonTypeEnergy;

import java.util.UUID;

public class CardBlueEyesWhiteDragon extends CardBaseMonster {

    public CardBlueEyesWhiteDragon(UUID owner, CardDescriptor descriptor) {
        super(owner, descriptor, new StatPower(descriptor.getBasePower()), new StatHealth(descriptor.getBaseHealth()),
                new CardMovementSquare(2, new FilterIncludeEnemyEmptyCell()),
                new SummonTypeEnergy(8));
    }
}
