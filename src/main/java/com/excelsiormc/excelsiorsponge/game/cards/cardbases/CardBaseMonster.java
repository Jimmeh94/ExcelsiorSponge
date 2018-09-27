package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.Stats;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public abstract class CardBaseMonster extends CardBase {

    public CardBaseMonster(UUID owner, double level, String name, CardRarity rarity, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, rarity, material, materialDamageValue, cardMovement);
    }
}
