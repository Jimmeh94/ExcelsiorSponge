package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.AltCodes;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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

            strings.add(Text.of(TextColors.RED, TextColors.GRAY, TextColors.BLUE));

            if(!cpp.getCurrentAim().get().isAvailable()){
                CardBase card = cpp.getCurrentAim().get().getOccupyingCard();

                strings.add(card.getName());
                strings.add(Text.of("------------------"));

                if(card.isOwner(getOwner().getOwner())){
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
                    //TODO display different info based on facing up or down
                    if(card.getCardFacePosition() == CardBase.CardFacePosition.FACE_UP) {
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
