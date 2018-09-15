package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.game.cards.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.CardMovementNormal;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummyTwo extends CardBase {

    public CardDummyTwo(UUID owner) {
        super(owner, 1, "Dummy 2 Card", BlockTypes.STONE, (short)1.0, new CardMovementNormal(1));
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
}
