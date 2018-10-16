package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.AltCodes;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseAvatar;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

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
        strings.add(Text.of(TextColors.GRAY, "==============="));
        strings.add(Text.of(TextColors.YELLOW, "Energy: " + cpp.getSummonEnergy()));

        if(cpp.getCurrentAim().isPresent() && cpp.getCurrentAim().get().getCellType() != null){
            strings.add(Text.of(TextColors.GOLD, "Cell terrain: " + StringUtils.enumToString(cpp.getCurrentAim().get().getCellType(),
                    true)));

            //Show card info
            if(!cpp.getCurrentAim().get().isAvailable() && !(cpp.getCurrentAim().get().getOccupyingCard() instanceof CardBaseAvatar)){
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

                    strings.add(card.getName());
                    strings.add(Text.of(TextColors.GRAY, "------------------"));

                    for(Text text: cpp.getCurrentAim().get().getOccupyingCard().getExtraDisplayInfo().values()){
                        strings.add(text);
                    }

                    strings.add(Text.builder().append(card.getPower().getDisplayName())
                            .append(Text.of(TextColors.GRAY, String.valueOf(card.getPower().getCurrent()))).build());
                    strings.add(Text.builder().append(card.getHealth().getDisplayName())
                            .append(Text.of(TextColors.GRAY, String.valueOf(card.getHealth().getCurrent()))).build());
                    strings.add(card.getDescriptor().getMovement());

                    strings.add(Text.of(TextColors.RED, TextColors.GREEN, TextColors.BLUE));
                    if(card.getDescriptor().description.toPlain().contains("Effect:")){
                        strings.addAll(StringUtils.getLongTextAsShortScoreboard(card.getDescriptor().getDescription()));
                    }
                } else {
                    if(card.getCardFacePosition() == CardBase.CardFacePosition.FACE_UP) {
                        strings.add(Text.of(TextColors.RED, TextColors.GRAY, TextColors.BLUE));
                        strings.add(card.getName());
                        strings.add(Text.of(TextColors.GRAY, "------------------"));

                        strings.add(Text.builder().append(card.getPower().getDisplayName())
                                .append(Text.of(TextColors.GRAY, String.valueOf(card.getPower().getCurrent()))).build());
                        strings.add(Text.builder().append(card.getHealth().getDisplayName())
                                .append(Text.of(TextColors.GRAY, String.valueOf(card.getHealth().getCurrent()))).build());
                        strings.add(card.getDescriptor().getMovement());

                        strings.add(Text.of(TextColors.RED, TextColors.YELLOW, TextColors.BLUE));
                        if(card.getDescriptor().description.toPlain().contains("Effect:")){
                            strings.addAll(StringUtils.getLongTextAsShortScoreboard(card.getDescriptor().getDescription()));
                        }
                    }
                }
            }

        }

        setScores(strings);
    }
}
