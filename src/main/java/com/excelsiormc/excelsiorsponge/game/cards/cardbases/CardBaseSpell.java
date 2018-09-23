package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public abstract class CardBaseSpell extends CardBase {

    public CardBaseSpell(UUID owner, double level, Text name, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, material, materialDamageValue, cardMovement);
    }
}
