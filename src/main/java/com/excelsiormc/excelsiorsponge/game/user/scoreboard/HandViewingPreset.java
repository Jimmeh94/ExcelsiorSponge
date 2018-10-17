package com.excelsiormc.excelsiorsponge.game.user.scoreboard;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.AltCodes;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandViewingPreset extends ScoreboardPreset {

    public HandViewingPreset(PlayerBase owner) {
        super(owner);

        updateScores();
    }

    @Override
    public void updateScores() {
        List<Text> strings = new ArrayList<>();
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(getOwner().getOwner()).get();

        strings.add(Text.of(TextColors.RED, AltCodes.CROSSED_SWORDS.getSign() + " " + TextColors.GRAY
                + DuelUtils.getArena(getOwner().getOwner()).get().getGamemode().getName() + " " + TextColors.RED + AltCodes.CROSSED_SWORDS.getSign()));
        strings.add(Text.of(TextColors.GRAY, "==============="));
        strings.add(Text.of(TextColors.YELLOW, "Energy: " + cpp.getSummonEnergy()));

        if(cpp.getCurrentAim().isPresent() && cpp.getCurrentAim().get().getCellType() != null){
            strings.add(Text.of(TextColors.GOLD, "Cell terrain: " + StringUtils.enumToString(cpp.getCurrentAim().get().getCellType(),
                    true)));

            Optional<CardBase> op =  cpp.getHand().getHeldCard();

            if(op.isPresent()){
                CardBase card = op.get();

                strings.add(Text.of(TextColors.GRAY, "------------------"));
                strings.add(Text.of(TextColors.RED, "Info for Held Card:"));
                strings.add(Text.of(TextColors.GRAY, "------------------"));

                if(cpp.getCurrentAim().isPresent() && cpp.getCurrentAim().get().isAvailable()){
                    Deck deck = cpp.getDeck();
                    if(deck.isAdvantageousTerrain(cpp.getCurrentAim().get().getCellType())){
                        strings.add(Text.builder().append(Text.of(TextColors.GOLD, "Terrain Effect: ")).append(Text.of(TextColors.GREEN, "^^")).build());
                    } else if(deck.isHinderingTerrain(cpp.getCurrentAim().get().getCellType())){
                        strings.add(Text.builder().append(Text.of(TextColors.GOLD, "Terrain Effect: ")).append(Text.of(TextColors.RED, "vv")).build());
                    } else {
                        strings.add(Text.builder().append(Text.of(TextColors.GOLD, "Terrain Effect: ")).append(Text.of(TextColors.GRAY, "--")).build());
                    }
                }
                strings.add(Text.of(TextColors.RED, TextColors.GRAY, TextColors.BLUE));

                strings.add(Text.builder().append(card.getName()).style(TextStyles.BOLD).build());
                strings.add(Text.of(TextColors.GRAY, "------------------"));

                strings.add(Text.builder().append(card.getPower().getDisplayName())
                        .append(Text.of(TextColors.GRAY, String.valueOf(card.getPower().getCurrent()))).build());
                strings.add(Text.builder().append(card.getHealth().getDisplayName())
                        .append(Text.of(TextColors.GRAY, String.valueOf(card.getHealth().getCurrent()))).build());
                strings.addAll(StringUtils.getLongTextAsShortScoreboard(card.getDescriptor().getSummon(), Optional.empty()));
                strings.add(card.getDescriptor().getMovement());

                strings.add(Text.of(TextColors.RED, TextColors.GREEN, TextColors.BLUE, TextColors.YELLOW));
                if(card.getDescriptor().description.toPlain().contains("Effect:")){
                    strings.addAll(StringUtils.getLongTextAsShortScoreboard(card.getDescriptor().getDescription(), Optional.of(TextColors.GRAY)));
                }
            }

        }

        setScores(strings);
    }

}
