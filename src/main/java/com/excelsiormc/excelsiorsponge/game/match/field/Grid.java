package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
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

    protected List<Row> rows;
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
        rows = new CopyOnWriteArrayList<>();
        border = new CopyOnWriteArrayList<>();

        GenerateCells(startingPos);

        if(drawGrid){
            drawGrid();
        }
    }

    public Optional<Cell> getCellInDirection(Cell current, Vector3d distanceInCells){
        double xDistance = cellDemX * distanceInCells.getX();
        xDistance += 1 * distanceInCells.getX();

        double zDistance = cellDemZ * distanceInCells.getZ();
        zDistance += 1 * distanceInCells.getZ();

        Vector3d target = current.getCenter().clone().add(new Vector3d(xDistance, 0, zDistance));
        if(isCell(target)){
            Cell cell = getCell(target).get();
            if(cell.isAvailable()){
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }

    public void drawGrid(){
        for(Row row: rows){
            row.drawCells();
        }
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for(Vector3d v: border){
            world.getLocation(v).setBlockType(gridBorder);
        }
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

    public List<Cell> getAvailableCellsCross(Cell current, int plusXDistanceInCells, int minusXDistanceInCells, int plusZDistanceInCels,
                                             int minusZDistanceInCells, boolean includeEnemyOccupiedCells, Team owner){
        Vector3d center = current.getCenter();
        List<Cell> give = new ArrayList<>();

        //get cell in +x, -x, +z, -z
        EditableVector start = new EditableVector(center.clone());
        EditableVector use = start.clone();

        //+ x
        for(int i = 1; i <= plusXDistanceInCells; i++){
            use.setX(use.getX() + (cellDemX) + 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }
        }

        //- x
        use = start.clone();
        for(int i = 1; i <= minusXDistanceInCells; i++){
            use.setX(use.getX() - (cellDemX) - 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }
        }

        //+ z
        use = start.clone();
        for(int i = 1; i <= plusZDistanceInCels; i++){
            use.setZ(use.getZ() + (cellDemZ) + 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }
        }

        //- z
        use = start.clone();
        for(int i = 1; i <= minusZDistanceInCells; i++){
            use.setZ(use.getZ() - (cellDemZ) - 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }
        }

        return give;
    }

    public List<Cell> getAvailableCellsEqualLengthCross(Cell current, int distanceInCells, boolean includeEnemyOccupiedCells, Team owner){
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

    public Row getRowFirst(){
        return rows.get(0);
    }

    public Row getRowLast(){
        return rows.get(rows.size() - 1);
    }
    public Optional<Row> getRowX(Cell cell){
        for(Row row: rows){
            if(row.containsCell(cell)){
                return Optional.of(row);
            }
        }
        return Optional.empty();
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

    public List<Row> getRows() {
        return rows;
    }

    public boolean isCell(Vector3d toVector3d) {
        for(Row row: rows){
            if(row.isWithinCell(toVector3d.toInt())){
                return true;
            }
        }
        return false;
    }

    public Optional<Cell> getCell(Vector3d toVector3d) {
        for(Row row: rows){
            if(row.isWithinCell(toVector3d.toInt())){
                return row.getCell(toVector3d.toInt());
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
        for(Row row: rows){
            row.resetCells();
        }
    }

    public List<CardBase> getActiveCardsOnFieldForTeam(Team team){
        List<CardBase> give = new ArrayList<>();

        for(Row row: rows){
            for(Cell cell: row.getCells()){
                if(!cell.isAvailable()){
                    if(team.isCombatant(cell.getOccupyingCard().getOwner())){
                        give.add(cell.getOccupyingCard());
                    }
                }
            }
        }

        return give;
    }

    public List<CardBase> getActiveCardsOnField(){
        List<CardBase> give = new ArrayList<>();

        for(Row row: rows) {
            for (Cell cell : row.getCells()) {
                if (!cell.isAvailable()) {
                    give.add(cell.getOccupyingCard());
                }
            }
        }

        return give;
    }
}
