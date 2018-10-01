package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementDiagonal;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyCell;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.UUID;

public class CardDummyThree extends CardBaseMonster {

    public CardDummyThree(UUID owner) {
        super(owner, 1, "Dummy 3 Card", CardRarity.LEGENDARY, ItemTypes.DIAMOND_HELMET, (short)1.0,
                new CardMovementDiagonal(2, new FilterIncludeEnemyCell(), new FilterIncludeEmptyCell()));
    }

    @Override
    protected Text getCardDescription() {
        return Text.of(TextColors.GRAY, TextStyles.ITALIC, "This card is a place holder for testing purposes.");
    }

    @Override
    protected void generateStats() {
        stats.addStat(StatIDs.HEALTH, new StatBase(200, 200, Text.of(TextColors.RED, "Health")));
        stats.addStat(StatIDs.ATTACK, new StatBase(100, 100, Text.of(TextColors.RED, "Attack Damage")));
        stats.addStat(StatIDs.CAST_COST, new StatBase(5, 5, Text.of(TextColors.RED, "Cast Cost")));
    }
}
