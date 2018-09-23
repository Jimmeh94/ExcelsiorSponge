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

    protected Stats<CardBase> stats;

    public CardBaseMonster(UUID owner, double level, Text name, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        super(owner, level, name, material, materialDamageValue, cardMovement);

        stats = new Stats<>(this);
        generateStats();
    }

    protected abstract void generateStats();

    public Stats<CardBase> getStats() {
        return stats;
    }

    public void displayStats(Player displayTo){
        Message.Builder builder = Message.builder();

        builder.addMessage(Text.of(TextColors.GRAY, "[-=======================================-]"));
        builder.addMessage(getName());
        builder.append(getLoreAsMessage());
        builder.addMessage(Text.of(" "));
        builder.append(Message.from(stats, getName(), displayTo));
        builder.addMessage(Text.of(TextColors.GRAY, "[-=======================================-]"));
        Messager.sendMessage(builder.build());
    }
}
