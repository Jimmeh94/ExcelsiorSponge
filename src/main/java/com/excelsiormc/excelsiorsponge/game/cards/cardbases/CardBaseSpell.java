package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public abstract class CardBaseSpell extends CardBase {

    public CardBaseSpell(UUID owner, double level, String name, CardRarity rarity, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, rarity, material, materialDamageValue, cardMovement);
    }

    @Override
    protected void generateStats() {
        stats.addStat(StatIDs.HEALTH, new StatBase(1, 1, Text.of(TextColors.RED, "Health")));
        stats.addStat(StatIDs.ATTACK, new StatBase(0, 0, Text.of(TextColors.AQUA, "Attack Damage")));
        stats.addStat(StatIDs.CAST_COST, new StatBase(1, 1, Text.of(TextColors.YELLOW, "Cast Cost")));
    }
}
