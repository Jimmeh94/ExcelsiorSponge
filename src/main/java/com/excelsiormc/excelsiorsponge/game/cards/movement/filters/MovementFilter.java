package com.excelsiormc.excelsiorsponge.game.cards.movement.filters;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class MovementFilter {

    private List<Cell> applicableCells;
    protected CardBase owner;

    public MovementFilter() {
        applicableCells = new CopyOnWriteArrayList<>();
    }

    /**
     * This should go through each cell, and if a cell meets this filter's
     * requirements, that cell will be added to applicable cells. When that
     * cell is then clicked during "movement", this action for that cell will fire.
     * @param cells
     */
    public abstract void filter(List<Cell> cells);

    /**
     * This is the action that will take place when this particular target is chosen
     * to move to.
     * @param target
     */
    public abstract void action(Cell target);

    /**
     * This will display the cells to the player
     */
    public abstract void drawCells(Player player);

    public boolean hasCell(Cell cell){
        return applicableCells.contains(cell);
    }

    protected void addCell(Cell cell){
        if(!applicableCells.contains(cell)){
            applicableCells.add(cell);
        }
    }

    public List<Cell> getApplicableCells() {
        return applicableCells;
    }

    public void clear(){
        this.applicableCells.clear();
    }

    public void setOwner(CardBase owner) {
        this.owner = owner;
    }
}
