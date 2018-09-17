package com.excelsiormc.excelsiorsponge.game.match;


import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.Optional;
import java.util.UUID;

public class Arena {

    private Gamemode gamemode;
    private Grid grid;
    private UUID id;
    private String world;
    private boolean inUse = false;

    public Arena(Grid grid, String world) {
        this(grid, world, UUID.randomUUID());
    }

    public Arena(Grid grid, String world, UUID id){
        this.grid = grid;
        this.world = world;
        this.id = id;
    }

    public void tick(){
        gamemode.baseTick();
    }

    public void updatePlayersAim() {
        gamemode.updatePlayersAim(grid);
    }

    public boolean isInUse() {
        return inUse;
    }

    public UUID getID() {
        return id;
    }

    public String getWorld() {
        return world;
    }

    public Optional<Integer> getGamemodeID() {
        if(gamemode == null){
            return Optional.empty();
        }
        return Optional.of(gamemode.getID());
    }

    public Grid getGrid() {
        return grid;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void end(){
        inUse = false;
        //TODO TP players out

        gamemode = null;
    }

    public void start(Gamemode gamemode) {
        inUse = true;
        this.gamemode = gamemode;
        this.gamemode.setArena(this);
        gamemode.start(new Location(Sponge.getServer().getWorld(world).get(), grid.getStartPos()));
    }

    public boolean isPlayerCombatant(Player player) {
        return gamemode.isPlayerCombatant(player);
    }

    public void playerQuit(Player player) {
        gamemode.playerQuit(player);
    }

    public void broadcastMessage(Text message, Messager.Prefix prefix){
        for(Team team: gamemode.getTeams()){
            for(CombatantProfile c: team.getCombatants()){
                if(c.isPlayer()){
                    Messager.sendMessage(c.getPlayer(), message, prefix);
                }
            }
        }
    }

    public Optional<CombatantProfile> getCombatantProfile(UUID uniqueId) {
        for(Team team: gamemode.getTeams()){
            for(CombatantProfile c: team.getCombatants()){
                if(c.getUUID().compareTo(uniqueId) == 0){
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }

    public boolean isPlayersTurn(Player player) {
        return gamemode.isPlayersTurn(player);
    }

    public boolean isCombatant(UUID uuid) {
        for(Team team: gamemode.getTeams()){
            for(CombatantProfile c: team.getCombatants()){
                if(c.getUUID().compareTo(uuid) == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public void handlePlayerRightEmptyClick() {
        gamemode.handlePlayerRightEmptyClick();
    }
}
