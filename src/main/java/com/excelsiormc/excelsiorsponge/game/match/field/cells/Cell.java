package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
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
    private Vector3d center, centerCeiling;
    private TerrainTypes cellType;
    private TerrainBuild build;

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
        centerCeiling = center.clone().add(0, 10, 0);
        
        ceiling = new CopyOnWriteArrayList<>();
        for(Vector3d v: locations){
            ceiling.add(v.clone().add(0, 10, 0));
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

        for(Vector3d v: ceiling){
            world.get().getLocation(v).setBlockType(BlockTypes.BARRIER);
        }
    }

    public List<Vector3d> getVector3ds() {
        return locations;
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

    public void setOccupyingCard(CardBase card, boolean spawn) {
        setAvailable(false);
        occupyingCard = card;
        if(spawn) {
            occupyingCard.spawn(new Location(Sponge.getServer().getWorld(world).get(), centerCeiling.getX(),
                    centerCeiling.getY() + 1, centerCeiling.getZ()));
        }
        occupyingCard.setCurrentCell(this);

        Optional<UserPlayer> up = PlayerUtils.getUserPlayer(card.getOwner());
        if(up.isPresent()){
            eraseAsPlaceable(up.get().getPlayer());
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

    public void eraseAsPlaceable(Player player) {
        for(Vector3d v: ceiling){
            player.sendBlockChange(v.toInt(), BlockState.builder().blockType(BlockTypes.BARRIER).build());
        }
    }

    public Vector3d getCenterCeiling() {
        return centerCeiling;
    }

    public void drawCustom(Player player, BlockState state) {
        for(Vector3d v: ceiling){
            player.sendBlockChange(v.toInt(), state);
        }
    }
}
