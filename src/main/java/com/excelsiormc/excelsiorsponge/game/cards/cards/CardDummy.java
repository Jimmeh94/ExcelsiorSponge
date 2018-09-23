package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.StatIDs;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummy extends CardBaseMonster {

    public CardDummy(UUID owner) {
        super(owner, 1, Text.of("Dummy Card"), ItemTypes.MAP, (short)1.0, new CardMovementNormal(1));
    }

    @Override
    protected List<Text> generateLore() {
        List<Text> give = new ArrayList<>();
        give.add(Text.of(TextColors.GRAY, "Rarity: Basic"));
        give.add(Text.of(TextColors.GRAY, "Level: 1"));
        give.add(Text.of( " "));
        give.add(Text.of(TextColors.GRAY, TextStyles.ITALIC, "This card is a place holder for testing purposes."));
        return give;
    }

    @Override
    protected void generateStats() {
        stats.addStat(StatIDs.HEALTH, new StatBase(100, 100, Text.of(TextColors.RED, "Health")));
        stats.addStat(StatIDs.ATTACK, new StatBase(1, 1, Text.of(TextColors.AQUA, "Attack Damage")));
        stats.addStat(StatIDs.CAST_COST, new StatBase(1, 1, Text.of(TextColors.YELLOW, "Cast Cost")));
    }
}
