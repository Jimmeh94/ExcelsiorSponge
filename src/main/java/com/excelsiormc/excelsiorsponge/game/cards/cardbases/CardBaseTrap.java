package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;
import org.spongepowered.api.item.ItemType;

import java.util.UUID;

public abstract class CardBaseTrap extends CardBase {

    public CardBaseTrap(UUID owner, double level, String name, CardRarity rarity, ItemType material,
                        int materialDamageValue, CardMovement cardMovement, SummonType summonType) {
        super(owner, level, name, rarity, new StatPower(0, 0), new StatHealth(0, 0),  material, materialDamageValue, cardMovement, summonType);
    }
}
