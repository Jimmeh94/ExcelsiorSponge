package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;
import org.spongepowered.api.item.ItemType;

import java.util.UUID;

public abstract class CardBaseMonster extends CardBase {

    public CardBaseMonster(UUID owner, double level, String name, CardRarity rarity, StatPower power, StatHealth health,
                           ItemType material, int materialDamageValue, CardMovement cardMovement, SummonType summonType) {
        super(owner, level, name, rarity, power, health, material, materialDamageValue, cardMovement, summonType);
    }
}
