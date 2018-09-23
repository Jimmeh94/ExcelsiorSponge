package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import org.spongepowered.api.Sponge;

import java.util.List;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;
    protected List<Cell> currentlyHighlighed;
    protected boolean canMoveThisTurn = true;

    public CardMovement(int distanceInCells) {
        this.distanceInCells = distanceInCells;
    }

    public void setOwner(CardBase owner) {
        this.owner = owner;
    }

    public abstract List<Cell> getAvailableSpaces();

    public List<Cell> getCurrentlyHighlighed() {
        return currentlyHighlighed;
    }

    public void setCurrentlyHighlighed(List<Cell> currentlyHighlighed) {
        this.currentlyHighlighed = currentlyHighlighed;
    }

    public void setCanMoveThisTurn(boolean canMoveThisTurn) {
        this.canMoveThisTurn = canMoveThisTurn;
    }

    public boolean canMoveThisTurn(){
        return canMoveThisTurn;
    }

    public void clearCurrentlyHighlighted(){
        for(Cell cell: currentlyHighlighed){
            cell.clearAimForPlayer(Sponge.getServer().getPlayer(owner.getOwner()).get());
        }
    }

    public boolean isAvailableSpace(Cell aim) {
        return currentlyHighlighed != null && currentlyHighlighed.contains(aim);
    }
}
