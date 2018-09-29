package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTemplate;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTypes;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Grid is made up of Cells
 */
public abstract class Grid {

    protected List<Row> rows;
    protected List<Vector3d> border;
    protected String world;
    protected int rowCount, rowLength, cellDim;
    protected final Vector3d startPos;
    protected BlockType gridBorder, cellMat;
    protected TerrainTemplate terrainTemplate;

    public Grid(Vector3d startingPos, String world, int rowCount, int rowLength, int cellDim, boolean drawGrid,
                BlockType gridBorder, BlockType cellMat){
        this.startPos = startingPos;
        this.world = world;
        this.rowCount = rowCount;
        this.rowLength = rowLength;
        this.cellDim = cellDim;
        this.gridBorder = gridBorder;
        this.cellMat = cellMat;
        rows = new CopyOnWriteArrayList<>();
        border = new CopyOnWriteArrayList<>();

        generateCells();
        drawCells();
        if(drawGrid){
            drawGrid();
        }
    }

    private void drawCells(){
        for(Row row: rows){
            row.drawCells();
        }
    }

    public void generateTerrain(){
        terrainTemplate = TerrainTypes.getNewTemplate(this);
        terrainTemplate.generateTerrain();
    }

    public Optional<Cell> getCellInDirection(Cell current, Vector3d distanceInCells, boolean useNearestIfEmpty){
        double xDistance = cellDim * distanceInCells.getX();
        xDistance += 1 * distanceInCells.getX();

        double zDistance = cellDim * distanceInCells.getZ();
        zDistance += 1 * distanceInCells.getZ();

        Vector3d target = current.getCenter().clone().add(new Vector3d(xDistance, 0, zDistance));
        if(isCell(target)){
            Cell cell = getCell(target).get();
            if(cell.isAvailable()){
                return Optional.of(cell);
            }
        } else if(useNearestIfEmpty){
            //This will get the cell nearest to the target
            EditableVector temp = new EditableVector(target);
            int x = xDistance < 0 ? 1 : -1;
            int z = zDistance < 0 ? 1 : -1;
            while(!isCell(temp.toVector3d())){
                temp.add(x, 0, z);
            }
            return Optional.of(getCell(temp.toVector3d()).get());
        }
        return Optional.empty();
    }

    public List<Cell> getAllCells(){
        List<Cell> cells = new CopyOnWriteArrayList<>();

        for(Row row: rows){
            cells.addAll(row.getCells());
        }

        return cells;
    }

    public void drawGrid(){
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for(Vector3d v: border){
            world.getLocation(v).setBlockType(gridBorder);
        }
    }

    public boolean areCellsInLine(Cell one, Cell two){
        Vector3i first = one.getCenter().toInt();
        Vector3i second = two.getCenter().toInt();

        return first.getX() == second.getX() || first.getZ() == second.getZ();
    }

    public List<Cell> getSquareGroupofCells(Cell start, int radiusInCells) {
        List<Cell> cells = new CopyOnWriteArrayList<>();

        EditableVector ev = new EditableVector(start.getCenter());
        double tempDistance = cellDim + 1;
        ev.setX(start.getCenter().getX() - (tempDistance * radiusInCells));
        ev.setZ(start.getCenter().getZ() - (tempDistance * radiusInCells));

        EditableVector use = ev.clone();

        //Now we have bottom right corner of area
        int limit = radiusInCells * 2;
        limit += 1;

        for(int i = 0; i < limit; i++){
            //this first loop will increase z by tempDistance each time
            for(int j = 0; j < limit; j++){
                //this second loop will increase x by tempDistance each time
                if(isCell(use.toVector3d())){
                    cells.add(getCell(use.toVector3d()).get());
                }
                use.setX(use.getX() + tempDistance);
            }
            use.setX(ev.getX());
            use.setZ(use.getZ() + tempDistance);
        }

        return cells;
    }

    public List<Cell> getSquareGroupofAvailableCells(Cell start, int radiusInCells, boolean includeEnemyOccupiedCells, Team owner) {
        List<Cell> cells = getSquareGroupofCells(start, radiusInCells);

        for(Cell cell: cells){
            if(!cell.isAvailable()){
                if(includeEnemyOccupiedCells && !owner.isCombatant(cell.getOccupyingCard().getOwner())){
                    continue;
                } else {
                    cells.remove(cell);
                }
            }
        }

        return cells;
    }

    public Row getRowInDirectionForLength(Cell start, int length, Vector3i direction){
        Row row = new Row();
        row.addCell(start);
        for(int i = 0; i < length; i++){
            Optional<Cell> cell = getCellInDirection(start, direction.toDouble(), false);
            if(cell.isPresent()){
                row.addCell(cell.get());
                start = cell.get();
            } else {
                return row;
            }
        }
        return row;
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
            use.setX(use.getX() + (cellDim) + 1);
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
            use.setX(use.getX() - (cellDim) - 1);
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
            use.setZ(use.getZ() + (cellDim) + 1);
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
            use.setZ(use.getZ() - (cellDim) - 1);
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
        List<Cell> give = new CopyOnWriteArrayList<>();
        give.add(current);

        //get cell in +x, -x, +z, -z
        EditableVector start = new EditableVector(center.clone());
        EditableVector use = start.clone();

        for(int i = 1; i <= distanceInCells; i++){
            use.setX(use.getX() + (cellDim * i) + 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }

            use = start.clone();
            use.setX(use.getX() - (cellDim * i) - 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }

            use = start.clone();
            use.setZ(use.getZ() - (cellDim * i) - 1);
            if(isCell(use.toVector3d())){
                Cell target = getCell(use.toVector3d()).get();
                if(target.isAvailable() || (includeEnemyOccupiedCells && !owner.isCombatant(target.getOccupyingCard().getOwner()))){
                    give.add(target);
                }
            }

            use = start.clone();
            use.setZ(use.getZ() + (cellDim * i) + 1);
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

    public Optional<Row> getHorizontalRow(Cell cell){
        for(Row row: rows){
            if(row.containsCell(cell)){
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    public Optional<Row> getVerticalRow(Cell cell){
        Row row = new Row();

        int index = -1;
        for(Row r: rows){
            if(r.containsCell(cell)){
                index = r.getCells().indexOf(cell);
            }
        }

        if(index > -1){
            for(Row r: rows){
                row.addCell(r.getCells().get(index));
            }
            return Optional.of(row);
        }

        return Optional.empty();
    }

    public boolean doAllCellsHaveTerrain(){
        for(Row row: rows){
            for(Cell cell: row.getCells()){
                if(cell.getCellType() == null){
                    return false;
                }
            }
        }
        return true;
    }

    public Cell getRandomCell(){
        Random random = new Random();
        return rows.get(random.nextInt(rows.size())).getCells().get(random.nextInt(rowLength));
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

    protected abstract void generateCells();

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
        return getCell(toVector3d.toInt());
    }

    public Optional<Cell> getCell(Vector3i v) {
        for(Row row: rows){
            if(row.isWithinCell(v)){
                return row.getCell(v);
            }
        }
        return Optional.empty();
    }

    public Vector3d getStartPos() {
        return startPos;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getRowLength(){
        return rowLength;
    }

    public int getCellDimension(){
        return cellDim;
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

    public List<Cell> getXGroupOfCells(int radius, Cell start) {
        List<Cell> cells = new CopyOnWriteArrayList<>();

        cells.addAll(getRowInDirectionForLength(start, radius, new Vector3i(1, 0, 1)).getCells());
        cells.addAll(getRowInDirectionForLength(start, radius, new Vector3i(1, 0, -1)).getCells());
        cells.addAll(getRowInDirectionForLength(start, radius, new Vector3i(-1, 0, -1)).getCells());
        cells.addAll(getRowInDirectionForLength(start, radius, new Vector3i(-1, 0, 1)).getCells());
        return cells;
    }
}
