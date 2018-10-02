package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseSpell;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyEmptyCell;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.UUID;

public class CardDummy extends CardBaseSpell {

    public CardDummy(UUID owner) {
        super(owner, 1, "Dummy Card", CardRarity.COMMON, ItemTypes.MAP, (short)1.0,
                new CardMovementNormal(1, new FilterIncludeEnemyEmptyCell()));
    }

    @Override
    protected Text getCardDescription() {
        return Text.of(TextColors.GRAY, TextStyles.ITALIC, "This card is a place holder for testing purposes.");
    }
}
