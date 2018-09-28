package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A cell is a # x # area of blocks within a Grid
 */
public class Cell {

    private List<Vector3d> locations;
    private String world;
    private BlockType material;
    private boolean isAvailable = true;
    private CardBase occupyingCard;
    private Vector3d center;
    private TerrainTypes cellType;

    public Cell(Vector3d startingPos, int dem, String world, BlockType material) {
        this.world = world;
        this.material = material;

        locations = new CopyOnWriteArrayList<>();

        EditableVector use = new EditableVector(startingPos.clone());
        int x = startingPos.getFloorX();
        int z = startingPos.getFloorZ();
        for(int i = 0; i < dem; i++, x++){
            use.setX(startingPos.getFloorX() + i);
            for(int j = 0; j < dem; j++, z++){
                use.setZ(z);
                locations.add(use.toVector3d());
            }
            z = startingPos.getFloorZ();
        }

        center = LocationUtils.getMiddleLocation(locations.get(0), locations.get(locations.size() - 1));
    }

    public void setCellType(TerrainTypes cellType) {
        this.cellType = cellType;
        drawCell();
    }

    public boolean isWithinCell(Vector3i check){
        for(Vector3d v: locations){
            Vector3i temp = v.toInt();
            if(temp.getX() == check.getX() && (temp.getY() == check.getY() || Math.abs(temp.getY() - check.getY()) <= 1) && temp.getZ() == check.getZ()){
                return true;
            }
        }
        return false;
    }

    public void drawCell(){
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for(Vector3d v: locations){
            if(cellType != null){
                world.getLocation(v).setBlockType(cellType.getCellType().getTerrainMat());
            } else {
                world.getLocation(v).setBlockType(material);
            }
        }
    }

    public void destroyCell(){
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for(Vector3d v: locations){
            world.getLocation(v).setBlockType(BlockTypes.AIR);
        }
    }

    public void drawAimForPlayer(Player player){
        for(Vector3d v: getVector3ds()){
            BlockState state = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build();
            player.sendBlockChange(v.toInt(), state.with(Keys.DYE_COLOR, DyeColors.RED).get());
        }
    }

    public void clearAimForPlayer(Player player){
        for (Vector3d v : getVector3ds()) {
            if(cellType != null){
                player.sendBlockChange(v.toInt(), BlockState.builder().blockType(cellType.getCellType().terrainMat).build());
            } else {
                player.sendBlockChange(v.toInt(), BlockState.builder().blockType(material).build());
            }
        }
    }

    public List<Vector3d> getVector3ds() {
        return locations;
    }

    public BlockType getMaterial() {
        return material;
    }

    public Vector3d getCenter() {
        return center.clone();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean b) {
        this.isAvailable = b;
        occupyingCard = null;
    }

    public CardBase getOccupyingCard() {
        return occupyingCard;
    }

    public void setOccupyingCard(CardBase card, boolean spawnArmorStand) {
        setAvailable(false);
        occupyingCard = card;
        if(spawnArmorStand) {
            occupyingCard.spawn3DRepresentationServer(new Location(Sponge.getServer().getWorld(world).get(), getCenter().getX(),
                    getCenter().getY() + 1, getCenter().getZ()));
        }
        occupyingCard.setCurrentCell(this);
    }

    /**
     * This will highlight the spaces where a card can move
     * @param player
     */
    public void drawAvailableSpaceForPlayer(Player player) {
        for(Vector3d v: getVector3ds()){
            BlockState state = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build();
            if(!isAvailable){
                //We can assume that this is an enemy occupied cell
                player.sendBlockChange(v.toInt(), state.with(Keys.DYE_COLOR, DyeColors.YELLOW).get());
            } else {
                player.sendBlockChange(v.toInt(), state.with(Keys.DYE_COLOR, DyeColors.BLUE).get());
            }
        }
    }

    public String getWorld() {
        return world;
    }

    public TerrainTypes getCellType() {
        return cellType;
    }
}
