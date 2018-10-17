package com.excelsiormc.excelsiorsponge.game.match.field;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTemplate;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.*;
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
        terrainTemplate = CellTerrains.getNewTemplate(this);
        terrainTemplate.generateTerrain();
    }

    public Optional<Cell> getCellInDirection(Cell current, Vector3i direction){
        double xDistance = cellDim * direction.getX();
        if(xDistance != 0) {
            xDistance += 1 * direction.getX();
        }

        double zDistance = cellDim * direction.getZ();
        if(zDistance != 0) {
            zDistance += 1 * direction.getZ();
        }

        Vector3d target = current.getCenter().clone().add(new Vector3d(xDistance, 0, zDistance));
        if(isCell(target)){
            return Optional.of(getCell(target).get());
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

    public Row getRowInDirectionForLength(Cell start, int lengthInCells, Vector3i direction){
        Row row = new Row();
        row.addCell(start);
        for(int i = 0; i < lengthInCells; i++){
            Optional<Cell> cell = getCellInDirection(start, direction);
            if(cell.isPresent()){
                row.addCell(cell.get());
                start = cell.get();
            } else {
                return row;
            }
        }
        return row;
    }

    public int getDistanceBetweenCells(Cell start, Cell end){
        return (int) (start.getCenter().distance(end.getCenter()) / (cellDim + 1));
    }

    public Row getRowBetweenCells(Cell start, Cell end){
        Vector3i direction = LocationUtils.getDirection(start.getCenter(), end.getCenter());

        Optional<Cell> current = Optional.of(start);
        Row row = new Row();

        while(current.isPresent() && current.get() != end){
            row.addCell(current.get());
            current = getCellInDirection(current.get(), direction);
        }
        if(current.isPresent() && !row.getCells().contains(current.get())){
            row.addCell(current.get());
        }
        return row;
    }

    public List<Cell> getCellsCross(Cell current, int plusXDistanceInCells, int minusXDistanceInCells, int plusZDistanceInCels,
                                    int minusZDistanceInCells){
        List<Cell> give = new CopyOnWriteArrayList<>();

        Row row = getRowInDirectionForLength(current, plusXDistanceInCells, new Vector3i(1, 0, 0));
        for(Cell cell: row.getCells()){
            if(!give.contains(cell)){
                give.add(cell);
            }
        }

        row = getRowInDirectionForLength(current, minusXDistanceInCells, new Vector3i(-1, 0, 0));
        for(Cell cell: row.getCells()){
            if(!give.contains(cell)){
                give.add(cell);
            }
        }

        row = getRowInDirectionForLength(current, plusZDistanceInCels, new Vector3i(0, 0, 1));
        for(Cell cell: row.getCells()){
            if(!give.contains(cell)){
                give.add(cell);
            }
        }

        row = getRowInDirectionForLength(current, minusZDistanceInCells, new Vector3i(0, 0, -1));
        for(Cell cell: row.getCells()){
            if(!give.contains(cell)){
                give.add(cell);
            }
        }

        return give;
    }

    public List<Cell> getCellsEqualLengthCross(Cell current, int distanceInCells){
        return getCellsCross(current, distanceInCells, distanceInCells, distanceInCells, distanceInCells);
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
        List<CardBase> give = new CopyOnWriteArrayList<>();

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
        List<CardBase> give = new CopyOnWriteArrayList<>();

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

    public Cell getCenterCell() {
        Row first = rows.get(0), last = rows.get(rows.size() - 1);
        return getCell(LocationUtils.getMiddleLocation(first.getCells().get(0).getCenter(), last.getCells().get(last.getCells().size() - 1).getCenter())).get();
    }

    public boolean isAimInCell(Vector3d pos) {
        Vector3i temp = pos.toInt();
        for(Row row: rows){
            for(Cell cell: row.getCells()){
                if(cell.isAimWithinCell(temp)){
                    return true;
                }
            }
        }
        return false;
    }

    public List<Cell> getCellsWithoutType() {
        List<Cell> give = new CopyOnWriteArrayList<>();
        for(Cell cell: getAllCells()){
            if(cell.getCellType() == null){
                give.add(cell);
            }
        }
        return give;
    }

    public void redrawGridForPlayer(Player player) {
        for(Cell cell: getAllCells()){
            if(cell.isAvailable()){
                cell.resetClientView(player);
            } else {
                Team team = DuelUtils.getTeam(player.getUniqueId());
                if(team.isCombatant(cell.getOccupyingCard().getOwner())){
                    if(cell.getOccupyingCard().isOwner(player.getUniqueId())){
                        cell.drawCustomOwner(player);
                    } else {
                        cell.drawCustomTeammate(player);
                    }
                } else {
                    cell.drawCustomEnemyNoCurrentThreat(player);
                }
            }
        }
    }

    public List<CardBase> getActiveCardsOnFieldFor(UUID owner) {
        List<CardBase> cards = new CopyOnWriteArrayList<>();
        for(Cell cell: getAllCells()){
            if(!cell.isAvailable()){
                if(cell.getOccupyingCard().isOwner(owner)){
                    cards.add(cell.getOccupyingCard());
                }
            }
        }
        return cards;
    }

    public boolean containsCard(CardBase card) {
        for(Cell cell: getAllCells()){
            if(!cell.isAvailable()){
                if(cell.getOccupyingCard() == card){
                    return true;
                }
            }
        }
        return false;
    }
}
