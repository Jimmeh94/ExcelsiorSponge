package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import org.spongepowered.api.item.ItemType;

import java.util.UUID;

public abstract class CardBaseSpell extends CardBase {

    public CardBaseSpell(UUID owner, double level, String name, CardRarity rarity, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, rarity, 0, 0, material, materialDamageValue, cardMovement);
    }
}
