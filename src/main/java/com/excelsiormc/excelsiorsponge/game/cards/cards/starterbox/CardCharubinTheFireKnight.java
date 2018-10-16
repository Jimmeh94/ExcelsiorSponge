package com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptors;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonFusion;

import java.util.UUID;

public class CardCharubinTheFireKnight extends CardBaseMonster {

    public CardCharubinTheFireKnight(UUID owner, CardDescriptor descriptor) {
        super(owner, descriptor, new StatPower(descriptor.getBasePower()), new StatHealth(descriptor.getBaseHealth()),
                new CardMovementNormal(1, new FilterIncludeEnemyEmptyCell()),
                new SummonFusion(CardDescriptors.MONSTER_EGG, CardDescriptors.HINOTAMA_SOUL));
    }

}
