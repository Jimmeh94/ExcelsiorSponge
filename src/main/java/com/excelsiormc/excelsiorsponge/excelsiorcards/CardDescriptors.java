package com.excelsiormc.excelsiorsponge.excelsiorcards;

import org.spongepowered.api.item.ItemTypes;

public enum CardDescriptors {

    /**
     * ======== Starter Box 1 =======
     */

    ANCIENT_TREE_OF_ENLIGHTENMENT(new CardDescriptor("Ancient Tree of Enlightenment", "Effect: While this card is face up" +
            " in defense position, enemy trap cards cannot be activated", CardDescriptor.CardType.PLANT, "Cross, 1 Cell",
            "3 Energy", 600, 1500, CardDescriptor.CardRarity.COMMON, ItemTypes.LOG, 100)),

    BASIC_INSECT(new CardDescriptor("Basic Insect", "These creatures are usually found traveling in swarms",
            CardDescriptor.CardType.INSECT, "Cross, 1 Cell","2 Energy", 700, 500,
            CardDescriptor.CardRarity.COMMON, ItemTypes.LEAVES, 100)),

    BLUE_EYES_WHITE_DRAGON(new CardDescriptor("Blue Eyes White Dragon", "This legendary dragon is a powerful" +
            "engine of destruction. Very few have faced this awesome creature and lived to tell the tale",
            CardDescriptor.CardType.DRAGON, "Square, 2 Cells","8 Energy", 2500, 3000,
            CardDescriptor.CardRarity.ULTRA_RARE, ItemTypes.DIAMOND, 100)),

    CHANGE_SLIME(new CardDescriptor("Change Slime", "Effect: A slime that can adopt the shape and stats" +
            " of any other monster in play (chooses at random)",
            CardDescriptor.CardType.DRAGON, "Cross, 1 Cell","1 Energy", 300, 400,
            CardDescriptor.CardRarity.COMMON, ItemTypes.SLIME_BALL, 100)),

    CHARUBIN_THE_FIRE_KNIGHT(new CardDescriptor("Charubin the Fire Knight", "A fearless knight of ancient flames",
                 CardDescriptor.CardType.PYRO, "Cross, 1 Cell","Fusion: Monster Egg, Hinotama Soul", 800, 1100,
                 CardDescriptor.CardRarity.COMMON, ItemTypes.BLAZE_POWDER, 100)),

    DARK_PRISONER(new CardDescriptor("Dark Prisoner", "Effect: This monster can hide its movements for 3 turns from the enemy",
            CardDescriptor.CardType.FIEND, "Cross, 1 Cell","3 Energy", 1000, 600,
            CardDescriptor.CardRarity.COMMON, ItemTypes.NETHER_WART, 100)),

    HINOTAMA_SOUL(new CardDescriptor("Hinotama Soul", "An intensely hot flame creature that rams anything standing" +
            "in its way", CardDescriptor.CardType.PYRO, "Cross, 1 Cell","2 Energy", 500, 600,
            CardDescriptor.CardRarity.COMMON, ItemTypes.BLAZE_POWDER, 100)),

    MONSTER_EGG(new CardDescriptor("Monster Egg", "A warrior hidden within an egg that attacks enemies by flinging eggshells",
            CardDescriptor.CardType.WARRIOR, "Cross, 1 Cell","3 Energy", 900, 600,
            CardDescriptor.CardRarity.COMMON, ItemTypes.EGG, 100));

    /**
     * ======== =======
     */

    private CardDescriptor cardDescriptor;

    CardDescriptors(CardDescriptor cardDescriptor) {
        this.cardDescriptor = cardDescriptor;
    }

    public CardDescriptor getCardDescriptor() {
        return cardDescriptor;
    }
}
