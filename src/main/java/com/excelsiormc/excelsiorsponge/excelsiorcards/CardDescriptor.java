package com.excelsiormc.excelsiorsponge.excelsiorcards;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that can be shared between the hub and arena plugins. Has all the basic info for a card.
 * Using this id, the arena plugin should be able to generate the corresponding class
 */
public class CardDescriptor {

    public Text name, description, type, movement, summon;
    public int baseHealth, basePower;
    protected CardRarity rarity;
    public ItemStack item;
    private ItemType mat;
    private int matDamageValue;

    public CardDescriptor(String name, String description, CardType cardType, String movement, String summon, int baseHealth,
                          int basePower, CardRarity rarity, ItemType mat, int matDamageValue) {
        this.name = Text.of(rarity.getColor(), name);
        if(description.contains("Effect")){
            this.description = Text.of(TextColors.GRAY, description);
        } else {
            this.description = Text.of(TextColors.GRAY, TextStyles.ITALIC, description);
        }
        this.type = cardType.getText();
        this.movement = Text.builder().append(Text.of(TextColors.GRAY, "Movement and Distance: ")).append(Text.of(TextColors.GRAY, movement)).build();
        this.summon = Text.builder().append(Text.of(TextColors.GRAY, "Summon Method: ")).append(Text.of(TextColors.GRAY, summon)).build();
        this.baseHealth = baseHealth;
        this.basePower = basePower;
        this.rarity = rarity;
        this.mat = mat;
        this.matDamageValue = matDamageValue;

        if(mat != null) {
            this.item = ItemStack.builder().itemType(mat).build();
            this.item.offer(Keys.DISPLAY_NAME, this.name);

            List<Text> give = new ArrayList<>();
            give.add(type);
            give.add(Text.builder().append(Text.of(TextColors.GRAY, "Rarity: "), rarity.getText()).build());
            give.add(Text.of( " "));
            give.add(this.description);
            give.add(Text.of( " "));
            give.add(Text.builder().append(Text.of(TextColors.RED, "Health: ")).append(Text.of(TextColors.GRAY, baseHealth)).build());
            give.add(Text.builder().append(Text.of(TextColors.YELLOW, "Power: ")).append(Text.of(TextColors.GRAY, basePower)).build());

            this.item.offer(Keys.ITEM_LORE, give);
            this.item.offer(Keys.ITEM_DURABILITY, matDamageValue);
        }
    }

    public ItemType getMat() {
        return mat;
    }

    public int getMatDamageValue() {
        return matDamageValue;
    }

    public ItemStack getItem() {
        return item;
    }

    public Text getName() {
        return name;
    }

    public Text getDescription() {
        return description;
    }

    public Text getType() {
        return type;
    }

    public Text getMovement() {
        return movement;
    }

    public Text getSummon() {
        return summon;
    }

    public double getBaseHealth() {
        return baseHealth;
    }

    public double getBasePower() {
        return basePower;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public enum CardRarity {
        ULTRA_RARE(TextColors.GOLD),
        SUPER_RARE(TextColors.LIGHT_PURPLE),
        RARE(TextColors.GREEN),
        COMMON(TextColors.GRAY);

        private TextColor color;

        CardRarity(TextColor color) {
            this.color = color;
        }

        public Text getText(){
            return Text.of(color, StringUtils.enumToString(this, true));
        }

        public TextColor getColor() {
            return color;
        }
    }

    public enum CardType {
        AQUA(Text.of(TextColors.AQUA, "Aqua")),
        BEAST(Text.of(TextColors.GRAY, "Beast")),
        BEAST_WARRIOR(Text.of(TextColors.GRAY, "Beast-Warrior")),
        DINOSAUR(Text.of(TextColors.GRAY, "Dinosaur")),
        DIVINE_BEAST(Text.of(TextColors.GOLD, "Divine-Beast")),
        DRAGON(Text.of(TextColors.BLUE, "Dragon")),
        FAIRY(Text.of(TextColors.WHITE, "Fairy")),
        FIEND(Text.of(TextColors.GREEN, "Fiend")),
        FISH(Text.of(TextColors.AQUA, "Fish")),
        INSECT(Text.of(TextColors.GREEN, "Insect")),
        MACHINE(Text.of(TextColors.GRAY, "Machine")),
        PLANT(Text.of(TextColors.GREEN, "Plant")),
        PSYCHIC(Text.of(TextColors.LIGHT_PURPLE, "Psychic")),
        PYRO(Text.of(TextColors.RED, "Pyro")),
        REPTILE(Text.of(TextColors.GREEN, "Reptile")),
        ROCK(Text.of(TextColors.GRAY, "Rock")),
        SEA_SERPENT(Text.of(TextColors.AQUA, "Sea Serpent")),
        SPELL(Text.of(TextColors.LIGHT_PURPLE, "Spell")),
        SPELLCASTER(Text.of(TextColors.LIGHT_PURPLE, "Spell Caster")),
        THUNDER(Text.of(TextColors.GOLD, "Thunder")),
        TRAP(Text.of(TextColors.LIGHT_PURPLE, "Trap")),
        WARRIOR(Text.of(TextColors.GRAY, "Warrior")),
        WINGED_BEAST(Text.of(TextColors.YELLOW, "Winged Beast")),
        ZOMBIE(Text.of(TextColors.GREEN, "Zombie"));

        private Text text;

        CardType(Text text) {
            this.text = text;
        }

        public Text getText() {
            if(this != SPELL && this != TRAP) {
                return Text.builder().append(Text.of(TextColors.GRAY, "Monster - ")).append(text).build();
            } else return text;
        }
    }

}
