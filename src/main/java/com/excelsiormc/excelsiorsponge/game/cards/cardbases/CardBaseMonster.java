package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public abstract class CardBaseMonster extends CardBase {

    public CardBaseMonster(UUID owner, String name, CardRarity rarity, StatPower power, StatHealth health,
                           ItemType material, int materialDamageValue, CardMovement cardMovement, SummonType summonType) {
        super(owner, name, rarity, power, health, material, materialDamageValue, cardMovement, summonType);
    }

    @Override
    protected Text getCardBaseType() {
        return Text.of(TextColors.GRAY, "Monster");
    }
}
