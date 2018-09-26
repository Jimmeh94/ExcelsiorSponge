package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public enum Gamemodes {

    DUEL(new GamemodeDescription(1, Text.of(TextColors.GRAY, TextStyles.ITALIC, "A 1v1 Gamemode")));

    private GamemodeDescription description;

    Gamemodes(GamemodeDescription description) {
        this.description = description;
    }

    public GamemodeDescription getDescription() {
        return description;
    }

    public static class GamemodeDescription{

        private int combatants;
        private Text description;

        public GamemodeDescription(int combatants, Text description) {
            this.combatants = combatants;
            this.description = description;
        }

        public int getCombatants() {
            return combatants;
        }

        public Text getDescription() {
            return description;
        }
    }

}
