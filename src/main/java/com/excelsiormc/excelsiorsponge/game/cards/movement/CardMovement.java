package com.excelsiormc.excelsiorsponge.game.cards.movement;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import org.spongepowered.api.Sponge;

import java.util.List;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;
    protected List<MovementEntry> currentlyHighlighted;
    protected boolean canMoveThisTurn = true;

    public CardMovement(int distanceInCells) {
        this.distanceInCells = distanceInCells;
    }

    public void setOwner(CardBase owner) {
        this.owner = owner;
    }

    public abstract List<MovementEntry> getAvailableSpaces();

    public List<MovementEntry> getCurrentlyHighlighted() {
        return currentlyHighlighted;
    }

    public void setCurrentlyHighlighted(List<MovementEntry> currentlyHighlighted) {
        this.currentlyHighlighted = currentlyHighlighted;
    }

    public void setCanMoveThisTurn(boolean canMoveThisTurn) {
        this.canMoveThisTurn = canMoveThisTurn;
    }

    public boolean canMoveThisTurn(){
        return canMoveThisTurn;
    }

    public void clearCurrentlyHighlighted(){
        for(MovementEntry entry: currentlyHighlighted){
            entry.cell.clearAimForPlayer(Sponge.getServer().getPlayer(owner.getOwner()).get());
        }
    }

    public boolean isAvailableSpace(Cell aim) {
        return currentlyHighlighted != null && currentlyHighlighted.contains(aim);
    }

    public MovementEntry getEntry(Cell aim) {
        for(MovementEntry entry: currentlyHighlighted){
            if(entry.isEntry(aim)){
                return entry;
            }
        }
        return null;
    }

    public static class MovementEntry {

        private Cell cell;
        private MovementEntryType type;

        public MovementEntry(Cell cell, MovementEntryType type) {
            this.cell = cell;
            this.type = type;
        }

        public Cell getCell() {
            return cell;
        }

        public boolean isEntry(Cell cell){
            return this.cell == cell;
        }

        public MovementEntryType getType() {
            return type;
        }

        public enum MovementEntryType {
            EMPTY,
            ENEMY_OCCUPIED
        }

    }
}
