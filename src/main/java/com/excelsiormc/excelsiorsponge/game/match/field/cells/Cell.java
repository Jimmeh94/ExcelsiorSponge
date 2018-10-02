package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.EditableVector;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.BlockStateColors;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
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
    private CellTerrains cellType;
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

    public void setCellType(CellTerrains cellType) {
        this.cellType = cellType;
        drawCell();
    }

    public void setBuild(TerrainBuild build) {
        this.build = build;
    }

    public boolean isWithinCell(Vector3i check){
        Vector3d temp = check.toDouble();
        for(int i = 0; i < locations.size(); i++){
            if(LocationUtils.isBetween(locations.get(i), ceiling.get(i).clone().add(0, 1, 0), temp,false)){
                return true;
            }
        }
        return false;
    }

    public boolean isAimWithinCell(Vector3i check){
        Vector3d temp = check.toDouble();
        for(int i = 0; i < ceiling.size(); i++){
            if(LocationUtils.isBetween(ceiling.get(i), ceiling.get(i).clone().add(0, 1, 0), temp,false)){
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
            cellType.getCellType().getBuild().draw(this, getVector3ds().get(0).clone().add(0, 1, 0));
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

        if(this.isAvailable) {
            if (occupyingCard != null){
                Optional<Player> player = PlayerUtils.getPlayer(occupyingCard.getOwner());
                if(player.isPresent()) {
                    eraseClient(player.get());

                    Gamemode gamemode = DuelUtils.getArena(occupyingCard.getOwner()).get().getGamemode();

                    for (Team team : gamemode.getTeams()) {
                        for (CombatantProfile c : team.getCombatants()) {
                            if (c.getUUID().compareTo(occupyingCard.getOwner()) == 0) {
                                continue;
                            }

                            if (c.isPlayer()) {
                                if (team.isCombatant(occupyingCard.getOwner())) {
                                    eraseClient(PlayerUtils.getPlayer(c.getUUID()).get());
                                } else {
                                    eraseClient(PlayerUtils.getPlayer(c.getUUID()).get());
                                }
                            }
                        }
                    }
                }
            }

            occupyingCard = null;
        }
    }

    public CardBase getOccupyingCard() {
        return occupyingCard;
    }

    public void setOccupyingCard(CardBase card, boolean spawn) {
        if(card == null){
            return;
        }

        setAvailable(false);
        occupyingCard = card;
        if(spawn) {
            occupyingCard.spawn(new Location(Sponge.getServer().getWorld(world).get(), centerCeiling.getX(),
                    centerCeiling.getY() + 1, centerCeiling.getZ()));
        }
        occupyingCard.setCurrentCell(this);

        Optional<UserPlayer> up = PlayerUtils.getUserPlayer(card.getOwner());
        if(up.isPresent()){
            eraseClient(up.get().getPlayer());
        }

        drawCustom(PlayerUtils.getPlayer(card.getOwner()).get(), BlockStateColors.OWNER);

        Gamemode gamemode = DuelUtils.getArena(card.getOwner()).get().getGamemode();

        for(Team team: gamemode.getTeams()){
            for(CombatantProfile c: team.getCombatants()){
                if(c.getUUID().compareTo(card.getOwner()) == 0){
                    continue;
                }

                if(c.isPlayer()){
                    if(team.isCombatant(card.getOwner())){
                        drawCustom(PlayerUtils.getPlayer(c.getUUID()).get(), BlockStateColors.TEAMMATE);
                    } else {
                        drawCustom(PlayerUtils.getPlayer(c.getUUID()).get(), BlockStateColors.ENEMY_NO_CURRENT_THREAT);
                    }
                }
            }
        }
    }

    public String getWorld() {
        return world;
    }

    public CellTerrains getCellType() {
        return cellType;
    }

    public void eraseBuild() {
        this.build.destroy(locations.get(0));
    }

    public void eraseAll(){
        eraseBuild();
        eraseCeiling();
    }

    public void eraseCeiling(){
        World world = Sponge.getServer().getWorld(getWorld()).get();
        for (Vector3d v : ceiling) {
            world.getLocation(v).setBlockType(material);
        }
    }

    public void eraseClient(Player player) {
        if(isAvailable) {
            for (Vector3d v : ceiling) {
                player.sendBlockChange(v.toInt(), BlockState.builder().blockType(BlockTypes.BARRIER).build());
            }
        } else if(DuelUtils.isCellEnemyOccupied(this, DuelUtils.getTeam(player.getUniqueId()))){
            drawCustom(player, BlockStateColors.ENEMY_NO_CURRENT_THREAT);
        }
    }

    public Vector3d getCenterCeiling() {
        return centerCeiling.clone().add(0, 1, 0);
    }

    public void drawCustom(Player player, BlockState state) {
        for(Vector3d v: ceiling){
            player.sendBlockChange(v.toInt(), state);
        }
    }

    public void reset() {
        if(occupyingCard != null){
            occupyingCard.remove();
        }
        setAvailable(true);
        eraseAll();
        setCellType(null);
    }
}
