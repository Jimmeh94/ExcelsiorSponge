package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.StatIDs;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummyTwo extends CardBaseMonster {

    public CardDummyTwo(UUID owner) {
        super(owner, 1, Text.of("Dummy 2 Card"), ItemTypes.STONE, (short)1.0, new CardMovementNormal(1));
    }

    @Override
    protected List<Text> generateLore() {
        List<Text> give = new ArrayList<>();
        give.add(Text.of(TextColors.GRAY, "Rarity: Rare"));
        give.add(Text.of(TextColors.GRAY, "Level: 2"));
        give.add(Text.of(" "));
        give.add(Text.of(TextColors.GRAY, TextStyles.ITALIC, "This card is a place holder for testing purposes"));
        return give;
    }

    @Override
    protected void generateStats() {
        stats.addStat(StatIDs.HEALTH, new StatBase(150, 150, Text.of(TextColors.RED, "Health")));
        stats.addStat(StatIDs.ATTACK, new StatBase(25, 25, Text.of(TextColors.RED, "Attack Damage")));
        stats.addStat(StatIDs.CAST_COST, new StatBase(2, 2, Text.of(TextColors.RED, "Cast Cost")));
    }
}
