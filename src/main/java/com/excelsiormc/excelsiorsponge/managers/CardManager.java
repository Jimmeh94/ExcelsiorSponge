package com.excelsiormc.excelsiorsponge.managers;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptors;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox.CardAncientTreeofEnlightenment;
import com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox.CardBasicInsect;
import com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox.CardBlueEyesWhiteDragon;

import java.util.UUID;

public class CardManager {

    public static CardBase getCardInstance(CardDescriptors descriptor, UUID owner){
        switch (descriptor){
            case ANCIENT_TREE_OF_ENLIGHTENMENT: return new CardAncientTreeofEnlightenment(owner, descriptor.getCardDescriptor());
            case BASIC_INSECT: return new CardBasicInsect(owner, descriptor.getCardDescriptor());
            case BLUE_EYES_WHITE_DRAGON: return new CardBlueEyesWhiteDragon(owner, descriptor.getCardDescriptor());
        }
        return null;
    }

}
