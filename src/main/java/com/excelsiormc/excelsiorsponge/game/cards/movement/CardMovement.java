package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.MovementFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;
    protected MovementFilter filter;
    protected boolean canMoveThisTurn = true;

    public CardMovement(int distanceInCells, MovementFilter filter) {
        this.distanceInCells = distanceInCells;
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
        List<Cell> cells = getAvailableSpaces();
        cells.remove(owner.getCurrentCell());
        filter.filter(cells);

        return getFilteredCells().size() > 0;
    }
}
