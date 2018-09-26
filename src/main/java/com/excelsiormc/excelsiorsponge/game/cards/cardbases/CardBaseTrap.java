package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public abstract class CardBaseTrap extends CardBase {

    public CardBaseTrap(UUID owner, double level, Text name, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, material, materialDamageValue, cardMovement);
    }

    @Override
    public void displayStats(Player displayTo) {

    }
}
