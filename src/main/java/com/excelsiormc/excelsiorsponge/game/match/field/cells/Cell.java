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
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A cell is a # x # area of blocks within a Grid
 */
public class Cell {

    private List<Vector3d> locations;
    private List<Vector3d> ceiling;
    private String world;
    private BlockType material;
    private boolean isAvailable = true;
    private CardBase occupyingCard;
    private Vector3d center;
    private TerrainTypes cellType;
    private TerrainBuild build;
    private Vector3d placeCardAt;

    public Cell(Vector3d startingPos, int dim, String world, BlockType material) {
        this.world = world;
        this.material = material;

        locations = new CopyOnWriteArrayList<>();

        EditableVector use = new EditableVector(startingPos.clone());
        int x = startingPos.getFloorX();
        int z = startingPos.getFloorZ();
        for(int i = 0; i < dim; i++, x++){
            use.setX(startingPos.getFloorX() + i);
            for(int j = 0; j < dim; j++, z++){
                use.setZ(z);
                locations.add(use.toVector3d());
            }
            z = startingPos.getFloorZ();
        }

        center = LocationUtils.getMiddleLocation(locations.get(0), locations.get(locations.size() - 1));

        placeCardAt = center.clone().add(0, 10, 0);
        ceiling = new CopyOnWriteArrayList<>(locations);
        for(Vector3d v: ceiling){
            ceiling.set(ceiling.indexOf(v), v.add(0, 10, 0));
        }
    }

    public void setCellType(TerrainTypes cellType) {
        this.cellType = cellType;
        drawCell();
    }

    public void setBuild(TerrainBuild build) {
        this.build = build;
    }

    public boolean isWithinCell(Vector3i check){
        for(Vector3d v: locations){
            Vector3i temp = v.toInt();
            if(temp.getX() == check.getX() && (temp.getY() == check.getY() || Math.abs(temp.getY() - check.getY()) <= 15) && temp.getZ() == check.getZ()){
                return true;
            }
        }
        return false;
    }

    public void drawCell(){
        Optional<World> world = Sponge.getServer().getWorld(getWorld());

        if(!world.isPresent()){
            return;
        }

        for(Vector3d v: locations){
            if(cellType != null){
                world.get().getLocation(v).setBlockType(cellType.getCellType().getTerrainMat());
            } else {
                world.get().getLocation(v).setBlockType(material);
            }
        }

        if(cellType != null){
            cellType.getCellType().getBuild().get(0).draw(this, getVector3ds().get(0).clone().add(0, 1, 0));
        }
    }

    public void destroyCell(){
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for(Vector3d v: locations){
            world.getLocation(v).setBlockType(BlockTypes.AIR);
        }
    }

    public void drawAimForPlayer(Player player){
        /*for(Vector3d v: ceiling){
            BlockState state = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build();
            player.sendBlockChange(v.toInt(), state.with(Keys.DYE_COLOR, DyeColors.RED).get());
        }*/
    }

    public void clearAimForPlayer(Player player){
        for (Vector3d v : ceiling) {
            //if(cellType != null){
                player.sendBlockChange(v.toInt(), BlockState.builder().blockType(BlockTypes.AIR).build());
            //} else {
              //  player.sendBlockChange(v.toInt(), BlockState.builder().blockType(material).build());
            //}
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
            occupyingCard.spawn3DRepresentationServer(new Location(Sponge.getServer().getWorld(world).get(), placeCardAt.getX(),
                    placeCardAt.getY() + 1, placeCardAt.getZ()));
        }
        occupyingCard.setCurrentCell(this);
    }

    /**
     * This will highlight the spaces where a card can move
     * @param player
     */
    public void drawAvailableSpaceForPlayer(Player player) {
        for(Vector3d v: ceiling){
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

    public void eraseBuild() {
        this.build.destroy(locations.get(0));
    }

    public Vector3d getPlaceCardAt() {
        return placeCardAt;
    }
}
