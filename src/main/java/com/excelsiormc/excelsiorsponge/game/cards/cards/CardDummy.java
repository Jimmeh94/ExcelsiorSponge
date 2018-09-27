package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseSpell;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummy extends CardBaseSpell {

    public CardDummy(UUID owner) {
        super(owner, 1, "Dummy Card", CardRarity.COMMON, ItemTypes.MAP, (short)1.0, new CardMovementNormal(1));
    }

    @Override
    protected Text getCardDescription() {
        return Text.of(TextColors.GRAY, TextStyles.ITALIC, "This card is a place holder for testing purposes.");
    }
}
