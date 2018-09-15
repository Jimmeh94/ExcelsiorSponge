package com.excelsiormc.excelsiorsponge.game.cards;

import com.excelsiormc.excelsiorsponge.game.match.field.Cell;

import java.util.List;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;
    protected List<Cell> currentlyHighlighed;

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

    public boolean isAvailableSpace(Cell aim) {
        return currentlyHighlighed != null && currentlyHighlighed.contains(aim);
    }
}
