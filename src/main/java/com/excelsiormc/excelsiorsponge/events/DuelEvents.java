package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.game.cards.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import org.spongepowered.api.event.Listener;

import java.util.List;

public class DuelEvents {

    @Listener
    public void onNewTurn(DuelEvent.BeginTurn event){
        Team team = event.getTeam();

        List<CardBase> cards = ExcelsiorSponge.INSTANCE.getArenaManager().findArenaWithCombatant(team.getCombatants().get(0).getUUID()).get()
                .getGrid().getActiveCardsOnFieldForTeam(team);
        for(CardBase c: cards){
            c.getMovement().setCanMoveThisTurn(true);
        }
    }

    @Listener
    public void onTurnEnd(DuelEvent.EndTurn event){

    }

}
