package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.StatEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class DuelEvents {

    @Listener
    public void onNewTurn(DuelEvent.BeginTurn event){
        Team team = event.getTeam();

        List<CardBase> cards = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(team.getCombatants().get(0).getUUID()).get()
                .getGrid().getActiveCardsOnFieldForTeam(team);
        for(CardBase c: cards){
            c.getMovement().setCanMoveThisTurn(true);
        }

        for(CombatantProfile c: team.getCombatants()){
            c.increaseSummonEnergy(2);

            if(c.isPlayer()){
                Messager.sendMessage(c.getPlayer(), Text.of(TextColors.GREEN, "+2 Summon Energy"), Messager.Prefix.DUEL);
            }
        }
    }

    @Listener
    public void onTurnEnd(DuelEvent.EndTurn event){
        Team team = event.getTeam();


    }

    @Listener
    public void onAimUpdated(DuelEvent.AimUpdated event){
        CombatantProfilePlayer cpp = event.getCombatPlayerProfile();

        //Update scoreboard with cell info
        cpp.getUserPlayer().updateScoreboard();
    }

    @Listener
    public void onStatEvent(StatEvent event){
        for(Arena arena: ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().getObjects()){
            for(CardBase c: arena.getGrid().getActiveCardsOnField()){
                if(c.hasStat(event.getStat())){
                    c.updateLore();
                }
            }
        }
    }

}
