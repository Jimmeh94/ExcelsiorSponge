package com.excelsiormc.excelsiorsponge.events;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.StatEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseCombatant;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
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

        for(CombatantProfile c: team.getCombatants()){
            if(c.isPlayer()){
                CombatantProfilePlayer cpp = (CombatantProfilePlayer) c;
                if(cpp.getUserPlayer().getPlayerMode() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                    cpp.stopMovingCard();
                }
            }
        }
    }

    @Listener
    public void onAimUpdated(DuelEvent.AimUpdated event){
        CombatantProfilePlayer cpp = event.getCombatPlayerProfile();

        //Update scoreboard with cell info
        cpp.getUserPlayer().updateScoreboard();

        //If occupied cell, send json chat link that when clicked shows details of card
        if(cpp.getCurrentAim().isPresent() && !cpp.getCurrentAim().get().isAvailable()){
            if(!(cpp.getCurrentAim().get().getOccupyingCard() instanceof CardBaseCombatant)) {
                PlayerUtils.sendCardToChat(cpp.getCurrentAim().get().getOccupyingCard(), cpp.getPlayer());
            }
        }
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
