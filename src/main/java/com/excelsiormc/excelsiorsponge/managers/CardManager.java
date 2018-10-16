package com.excelsiormc.excelsiorsponge.managers;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptors;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox.*;

import java.util.UUID;

public class CardManager {

    public static CardBase getCardInstance(CardDescriptors descriptor, UUID owner){
        switch (descriptor){
            case ANCIENT_TREE_OF_ENLIGHTENMENT: return new CardAncientTreeofEnlightenment(owner, descriptor.getCardDescriptor());
            case BASIC_INSECT: return new CardBasicInsect(owner, descriptor.getCardDescriptor());
            case BLUE_EYES_WHITE_DRAGON: return new CardBlueEyesWhiteDragon(owner, descriptor.getCardDescriptor());
            case CHANGE_SLIME: return new CardChangeSlime(owner, descriptor.getCardDescriptor());
            case CHARUBIN_THE_FIRE_KNIGHT: return new CardCharubinTheFireKnight(owner, descriptor.getCardDescriptor());
            case DARK_PRISONER: return new CardDarkPrisoner(owner, descriptor.getCardDescriptor());
            case HINOTAMA_SOUL: return new CardHinotamaSoul(owner, descriptor.getCardDescriptor());
            case MONSTER_EGG: return new CardMonsterEgg(owner, descriptor.getCardDescriptor());

            default: return null;
        }
    }

}
