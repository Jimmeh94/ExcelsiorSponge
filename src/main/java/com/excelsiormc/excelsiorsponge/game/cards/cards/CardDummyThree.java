package com.excelsiormc.excelsiorsponge.game.cards.cards;

import com.excelsiormc.excelsiorsponge.game.cards.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.CardMovementDiagonal;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummyThree extends CardBase {

    public CardDummyThree(UUID owner) {
        super(owner, 1, "Dummy 3 Card", ItemTypes.DIAMOND_HELMET, (short)1.0, new CardMovementDiagonal(2));
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
}
