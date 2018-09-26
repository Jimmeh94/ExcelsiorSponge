package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.event.Listener;

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
    }

    @Listener
    public void onTurnEnd(DuelEvent.EndTurn event){

    }

    @Listener
    public void onAimUpdated(DuelEvent.AimUpdated event){
        CombatantProfilePlayer cpp = event.getCombatPlayerProfile();

        //Update scoreboard with cell info
        cpp.getUserPlayer().updateScoreboard();

        //If occupied cell, send json chat link that when clicked shows details of card
        if(!cpp.getCurrentAim().isAvailable()){
            PlayerUtils.sendCardToChat(cpp.getCurrentAim().getOccupyingCard(), cpp.getPlayer());
        }

    }

}
