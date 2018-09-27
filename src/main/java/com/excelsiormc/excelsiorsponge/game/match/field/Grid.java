package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.utils.EditableVector;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Grid is made up of Cells
 */
public abstract class Grid {

    protected List<Cell> cells;
    protected List<Vector3d> border;
    protected String world;
    protected int gridDemX, gridDemZ, cellDemX, cellDemZ;
    protected Vector3d startPos;
    protected BlockType gridBorder, cellMat;


    public Grid(Vector3d startingPos, String world, int gridDemX, int gridDemZ, int cellDemX, int cellDemZ, boolean drawGrid,
                BlockType gridBorder, BlockType cellMat){
        this.startPos = startingPos;
        this.world = world;
        this.gridDemX = gridDemX;
        this.gridDemZ = gridDemZ;
        this.cellDemX = cellDemX;
        this.cellDemZ = cellDemZ;
        this.gridBorder = gridBorder;
        this.cellMat = cellMat;
        cells = new CopyOnWriteArrayList<>();
        border = new CopyOnWriteArrayList<>();

        GenerateCells(startingPos);

        if(drawGrid){
            drawGrid();
        }
    }

    public Optional<Cell> getCellInDirection(Cell current, Vector3d direction){
        double xDistance = cellDemX * direction.getX();
        xDistance += 1 * direction.getX();

        double zDistance = cellDemZ * direction.getZ();
        zDistance += 1 * direction.getZ();

        //direction.setX(direction.getX() * ((cellDemX * direction.getX()) + (1 * direction.getX())));
        //direction.setZ(direction.getZ() * ((cellDemZ * direction.getZ()) + (1 * direction.getZ())));

        Vector3d target = current.getCenter().clone().add(new Vector3d(xDistance, 0, zDistance));
        if(isCell(target)){
            Cell cell = getCell(target).get();
            if(cell.isAvailable()){
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }

    public List<Cell> getSquareGroupofCells(Vector3d start, int distanceInCells, boolean includeEnemyOccupiedCells, Team owner) {
        List<Cell> cells = new ArrayList<>();

        EditableVector ev = new EditableVector(start);
        double tempDistance = cellDemX + 1;
        ev.setX(start.getX() - (tempDistance * distanceInCells));
        ev.setZ(start.getZ() - (tempDistance * distanceInCells));

        EditableVector use = ev.clone();

        //Now we have bottom right corner of area
        int limit = distanceInCells * 2;
        limit += 1;

        for(int i = 0; i < limit; i++){
            //this first loop will increase z by tempDistance each time
            for(int j = 0; j < limit; j++){
                //this second loop will incrase x by tempDistance each time
                if(isCell(use.toVector3d())){
                    Cell cell = getCell(use.toVector3d()).get();
                    if(cell.isAvailable()){
                        cells.add(cell);
                    } else if(includeEnemyOccupiedCells && !owner.isCombatant(cell.getOccupyingCard().getOwner())){
                        cells.add(cell);
                    }
                }
                use.setX(use.getX() + tempDistance);
            }
            use.setX(ev.getX());
            use.setZ(use.getZ() + tempDistance);
        }

        return cells;
    }

    public void drawGrid(){
        for(Cell cell: cells){
            cell.drawCell();
        }
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for(Vector3d v: border){
            world.getLocation(v).setBlockType(gridBorder);
        }
    }

    public List<Cell> getAdjacentAvailableCells(Cell current, int distanceInCells, boolean includeEnemyOccupiedCells, Team owner){
        Vector3d center = current.getCenter();
        List<Cell> give = new ArrayList<>();

        //get cell in +x, -x, +z, -z
        EditableVector start = new EditableVector(center.clone());
        EditableVector use = start.clone();

        for(int i = 1; i <= distanceInCells; i++){
            use.setX(use.getX() + (cellDemX * i) + 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }

            use = start.clone();
            use.setX(use.getX() - (cellDemX* i) - 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }

            use = start.clone();
            use.setZ(use.getZ() - (cellDemZ * i) - 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }

            use = start.clone();
            use.setZ(use.getZ() + (cellDemZ * i) + 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }
        }

        return give;
    }

    public BlockType getGridBorder() {
        return gridBorder;
    }

    public BlockType getCellMat() {
        return cellMat;
    }

    public String getWorld() {
        return world;
    }

    protected abstract void GenerateCells(Vector3d startingPos);

    public List<Cell> getCells() {
        return cells;
    }

    public boolean isCell(Vector3d toVector3d) {
        for(Cell c: cells){
            if(c.isWithinCell(toVector3d.toInt())){
                return true;
            }
        }
        return false;
    }

    public Optional<Cell> getCell(Vector3d toVector3d) {
        for(Cell c: cells){
            if(c.isWithinCell(toVector3d.toInt())){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Vector3d getStartPos() {
        return startPos;
    }

    public int getGridX() {
        return gridDemX;
    }

    public int getGridZ(){
        return gridDemZ;
    }

    public int getCellX(){
        return cellDemX;
    }

    public int getCellZ(){
        return cellDemZ;
    }

    public void resetCells() {
        for(Cell cell: cells){
            cell.setAvailable(true);
        }
    }

    public void displayAvailableCellsToMoveTo(CardBase cardBase) {
        List<CardMovement.MovementEntry> cells = cardBase.getMovement().getAvailableSpaces();
        cardBase.getMovement().setCurrentlyHighlighted(cells);
        for(CardMovement.MovementEntry entry: cells){
            entry.getCell().drawAvailableSpaceForPlayer(Sponge.getServer().getPlayer(cardBase.getOwner()).get());
        }
    }

    public void eraseAvailableCellsToMoveTo(CardBase cardBase){
        for(Cell cell: cells){
            cell.clearAimForPlayer(Sponge.getServer().getPlayer(cardBase.getOwner()).get());
        }
    }

    public List<CardBase> getActiveCardsOnFieldForTeam(Team team){
        List<CardBase> give = new ArrayList<>();

        for(Cell cell: cells){
            if(!cell.isAvailable()){
                if(team.isCombatant(cell.getOccupyingCard().getOwner())){
                    give.add(cell.getOccupyingCard());
                }
            }
        }

        return give;
    }

    public List<CardBase> getActiveCardsOnField(){
        List<CardBase> give = new ArrayList<>();

        for(Cell cell: cells){
            if(!cell.isAvailable()){
                give.add(cell.getOccupyingCard());
            }
        }

        return give;
    }
}
