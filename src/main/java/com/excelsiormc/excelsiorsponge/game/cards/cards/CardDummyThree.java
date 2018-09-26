package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementDiagonal;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummyThree extends CardBaseMonster {

    public CardDummyThree(UUID owner) {
        super(owner, 1, Text.of("Dummy 3 Card"), ItemTypes.DIAMOND_HELMET, (short)1.0, new CardMovementDiagonal(2));
    }

    @Override
    protected List<Text> generateLore() {
        List<Text> give = new ArrayList<>();
        give.add(Text.of(TextColors.GRAY, "Rarity: Legendary"));
        give.add(Text.of(TextColors.GRAY, "Level: 3"));
        give.add(Text.of(" "));
        give.add(Text.of(TextColors.GRAY, TextStyles.ITALIC.toString() + "This card is a place holder for testing purposes."));
        return give;
    }

    @Override
    protected void generateStats() {
        stats.addStat(StatIDs.HEALTH, new StatBase(200, 200, Text.of(TextColors.RED, "Health")));
        stats.addStat(StatIDs.ATTACK, new StatBase(100, 100, Text.of(TextColors.RED, "Attack Damage")));
        stats.addStat(StatIDs.CAST_COST, new StatBase(5, 5, Text.of(TextColors.RED, "Cast Cost")));
    }
}
