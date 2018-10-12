package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.AltCodes;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseCombatant;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArenaDefaultPreset extends ScoreboardPreset {

    private Arena arena;

    public ArenaDefaultPreset(PlayerBase owner, Arena arena) {
        super(owner);

        this.arena = arena;
        updateScores();
    }

    @Override
    public void updateScores() {
        List<Text> strings = new ArrayList<>();
        CombatantProfilePlayer cpp = (CombatantProfilePlayer) arena.getCombatantProfile(getOwner().getOwner()).get();

        strings.add(Text.of(TextColors.RED, AltCodes.CROSSED_SWORDS.getSign() + " " + TextColors.GRAY 
                + arena.getGamemode().getName() + " " + TextColors.RED + AltCodes.CROSSED_SWORDS.getSign()));
        strings.add(Text.of("==============="));
        strings.add(Text.of(TextColors.RED));
        strings.add(Text.of(TextColors.YELLOW, "Energy: " + cpp.getSummonEnergy()));

        if(cpp.getCurrentAim().isPresent() && cpp.getCurrentAim().get().getCellType() != null){
            strings.add(Text.of(TextColors.GOLD, "Cell terrain: " + StringUtils.enumToString(cpp.getCurrentAim().get().getCellType(),
                    true)));

            //Show card info
            if(!cpp.getCurrentAim().get().isAvailable() && !(cpp.getCurrentAim().get().getOccupyingCard() instanceof CardBaseCombatant)){
                CardBase card = cpp.getCurrentAim().get().getOccupyingCard();

                if(card.isOwner(getOwner().getOwner())){

                    Deck deck = cpp.getDeck();
                    if(deck.isAdvantageousTerrain(cpp.getCurrentAim().get().getCellType())){
                        strings.add(Text.builder().append(Text.of(TextColors.GOLD, "Terrain Effect: ")).append(Text.of(TextColors.GREEN, "^^")).build());
                    } else if(deck.isHinderingTerrain(cpp.getCurrentAim().get().getCellType())){
                        strings.add(Text.builder().append(Text.of(TextColors.GOLD, "Terrain Effect: ")).append(Text.of(TextColors.RED, "vv")).build());
                    } else {
                        strings.add(Text.builder().append(Text.of(TextColors.GOLD, "Terrain Effect: ")).append(Text.of(TextColors.GRAY, "--")).build());
                    }
                    strings.add(Text.of(TextColors.RED, TextColors.GRAY, TextColors.BLUE));

                    strings.add(Text.of(TextStyles.BOLD, card.getName()));
                    strings.add(Text.of("------------------"));

                    List<Text> texts = new CopyOnWriteArrayList<>(card.getLore());
                    for(Text text: texts){
                        if(text.toPlain().length() > 40){
                            String t = text.toPlain();
                            String[] temp = new String[t.length() % 30 > 0 ? t.length() / 30 + 1 : t.length() / 30];
                            for(int i = 0; i < temp.length; i++){
                                if(i < temp.length - 1) {
                                    temp[i] = t.substring(i * 30, 30 + (30 * i));
                                } else {
                                    temp[i] = t.substring(i * 30);
                                }
                            }
                            for(int i = 0; i < temp.length; i++){
                                texts.add(texts.indexOf(text) + i, Text.of(text.getColor(), text.getFormat(), temp[i]));
                            }
                            texts.remove(text);
                        }
                    }
                    strings.addAll(texts);
                } else {
                    if(card.getCardFacePosition() == CardBase.CardFacePosition.FACE_UP) {
                        strings.add(Text.of(TextColors.RED, TextColors.GRAY, TextColors.BLUE));
                        strings.add(card.getName());
                        strings.add(Text.of("------------------"));

                        List<Text> texts = new CopyOnWriteArrayList<>(card.getLore());
                        for (Text text : texts) {
                            if (text.toPlain().length() > 40) {
                                String t = text.toPlain();
                                String[] temp = new String[t.length() % 30 > 0 ? t.length() / 30 + 1 : t.length() / 30];
                                for (int i = 0; i < temp.length; i++) {
                                    if (i < temp.length - 1) {
                                        temp[i] = t.substring(i * 30, 30 + (30 * i));
                                    } else {
                                        temp[i] = t.substring(i * 30);
                                    }
                                }
                                for (int i = 0; i < temp.length; i++) {
                                    texts.add(texts.indexOf(text) + i, Text.of(text.getColor(), text.getFormat(), temp[i]));
                                }
                                texts.remove(text);
                            }
                        }
                        strings.addAll(texts);
                    }
                }
            }

        }

        setScores(strings);
    }
}
