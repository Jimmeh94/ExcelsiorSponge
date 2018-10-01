package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.MovementFilter;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;
    protected List<MovementFilter> filters;
    protected boolean canMoveThisTurn = true;

    public CardMovement(int distanceInCells, MovementFilter... filters) {
        this.distanceInCells = distanceInCells;
        this.filters = Arrays.asList(filters);
    }

    public abstract List<Cell> getAvailableSpaces();

    public void setOwner(CardBase owner) {
        this.owner = owner;

        for(MovementFilter filter: filters){
            filter.setOwner(owner);
        }
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
            cell.eraseAsPlaceable(player);
        }

        for(MovementFilter filter: filters){
            filter.clear();
        }
    }

    public boolean isAvailableSpace(Cell aim) {
        for(MovementFilter filter: filters){
            if(filter.hasCell(aim)){
                return true;
            }
        }
        return false;
    }

    protected List<Cell> getFilteredCells(){
        List<Cell> give = new CopyOnWriteArrayList<>();
        for(MovementFilter filter: filters){
            give.addAll(filter.getApplicableCells());
        }
        return give;
    }

    public void displayAvailableSpotsToMoveTo() {
        Player player = PlayerUtils.getPlayer(owner.getOwner()).get();
        for(MovementFilter filter: filters){
            filter.drawCells(player);
        }
    }

    public void handle(Cell aim) {
        for(MovementFilter filter: filters){
            if(filter.hasCell(aim)){
                filter.action(aim);
            }
        }
    }

    public boolean generateSpots() {
        List<Cell> cells = getAvailableSpaces();

        cells.remove(owner.getCurrentCell());
        for(MovementFilter filter: filters){
            filter.filter(cells);
        }

        return getFilteredCells().size() > 0;
    }
}
