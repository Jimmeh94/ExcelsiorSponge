package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import org.spongepowered.api.item.ItemType;

import java.util.UUID;

public abstract class CardBaseMonster extends CardBase {

    public CardBaseMonster(UUID owner, double level, String name, CardRarity rarity, double attack, double health,
                           ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, rarity, attack, health, material, materialDamageValue, cardMovement);
    }
}
