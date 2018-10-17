package com.excelsiormc.excelsiorsponge.utils;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrain;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DuelUtils {

    public static void displayCellInfo(Player player){
        ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player).get().handlePlayerEmptyClick(player);
    }

    public static void moveCardToCell(Player player){
        CombatantProfilePlayer cpp = getCombatProfilePlayer(player.getUniqueId()).get();
        moveCardToCell(cpp.getCurrentAim().get(), player);
    }

    public static void moveCardToCell(Cell target, Player owner){
        CombatantProfilePlayer cpp = getCombatProfilePlayer(owner.getUniqueId()).get();
        CardBase card = cpp.getCurrentlyMovingCard();

        if(target != null && card != null){
            Cell old = card.getCurrentCell();
            old.setAvailable(true);
            target.setOccupyingCard(card, false);
            card.move(target.getCenterCeiling(), old);
        }
    }

    public static Cell getCellWithoutTerrainOrOverrideable(CellTerrain terrain, Grid grid){
        Cell start = grid.getRandomCell();

        while(start.getCellType() != null && !start.getCellType().canBeOverriddenBy(terrain)){
            start = grid.getRandomCell();
        }

        return start;
    }

    public static Optional<Arena> getArena(UUID uuid){
        return ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(uuid);
    }

    public static boolean isCellEnemyOccupied(Cell cell, Team ally){
        return !cell.isAvailable() && !ally.isCombatant(cell.getOccupyingCard().getOwner());
    }

    public static Team getTeam(UUID combatant){
        return ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(combatant).get().getGamemode().getTeamWithCombatant(combatant);
    }

    public static Optional<Arena> findArenaWithPlayer(Player player){
        return ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player);
    }

    public static Optional<CombatantProfilePlayer> getCombatProfilePlayer(UUID uuid){
        Optional<CombatantProfile> c = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(uuid).get().getCombatantProfile(uuid);

        return c.isPresent() && c.get().isPlayer() ? Optional.of((CombatantProfilePlayer)c.get()) : Optional.empty();
    }

    public static Deck getDeck(UUID owner) {
        return getCombatProfilePlayer(owner).get().getDeck();
    }

    public static List<CardBase> getCombatantsPlayedCards(UUID owner) {
        return getArena(owner).get().getGrid().getActiveCardsOnFieldFor(owner);
    }

    public static List<CardBase> filterCardsForMonsters(List<CardBase> cards) {
        for(CardBase c: cards){
            if(!(c instanceof CardBaseMonster)){
                cards.remove(c);
            }
        }
        return cards;
    }

    public static List<CardBase> getAllActiveCards(UUID owner){
        return getArena(owner).get().getGrid().getActiveCardsOnField();
    }

    public static boolean isActiveTurn(Player player) {
        return getArena(player.getUniqueId()).get().getGamemode().isPlayersTurn(player);
    }
}
