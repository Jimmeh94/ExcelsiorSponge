package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.MovementFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;
    protected final int originalDistance;
    protected MovementFilter filter;
    protected boolean canMoveThisTurn = true;

    public CardMovement(int distanceInCells, MovementFilter filter) {
        this.distanceInCells = distanceInCells;
        originalDistance = distanceInCells;
        this.filter = filter;
    }

    public abstract List<Cell> getAvailableSpaces();

    public void setOwner(CardBase owner) {
        this.owner = owner;

        filter.setOwner(owner);
    }

    public void setCanMoveThisTurn(boolean canMoveThisTurn) {
        this.canMoveThisTurn = canMoveThisTurn;
    }

    public boolean canMoveThisTurn(){
        return canMoveThisTurn;
    }

    public void clearCurrentlyHighlighted(){
        Player player = PlayerUtils.getPlayer(owner.getOwner()).get();
        for(Cell cell: getFilteredCells()){
            cell.eraseClient(player);
        }

        filter.clear();
    }

    public boolean isAvailableSpace(Cell aim) {
        return filter.hasCell(aim);
    }

    protected List<Cell> getFilteredCells(){
        return filter.getApplicableCells();
    }

    public void displayAvailableSpotsToMoveTo() {
        filter.drawCells(PlayerUtils.getPlayer(owner.getOwner()).get());
    }

    public void handle(Cell aim) {
        filter.action(aim);
    }

    public boolean generateSpots() {
        Deck deck = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get().getDeck();
        if(deck.isHinderingTerrain(owner.getCurrentCell().getCellType())){
            distanceInCells = 1;
        } else if(deck.isAdvantageousTerrain(owner.getCurrentCell().getCellType())){
            distanceInCells = originalDistance + 1;
        } else {
            distanceInCells = originalDistance;
        }

        List<Cell> cells = getAvailableSpaces();
        cells.remove(owner.getCurrentCell());
        filter.filter(cells);

        return getFilteredCells().size() > 0;
    }
}
