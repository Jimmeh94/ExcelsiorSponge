package com.excelsiormc.excelsiorsponge.game.match;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel.HotbarHand;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Team {

    private List<CombatantProfile> combatants;
    private Vector3d spawn;
    private boolean alive = true;
    private Cell spawnCell;
    private List<UUID> votesToEndTurn;

    public Team() {
        combatants = new CopyOnWriteArrayList<>();
        votesToEndTurn = new ArrayList<>();
    }

    public Team(CombatantProfile... combatants){
        this.combatants = new CopyOnWriteArrayList<>();
        this.combatants.addAll(Arrays.asList(combatants));
        votesToEndTurn = new ArrayList<>();
    }

    public void addCombatant(CombatantProfile profile){
        combatants.add(profile);
    }

    public void broadcastStartTurnMessage() {
        for(CombatantProfile c: combatants){
            if(c.isPlayer()){
                Messager.sendTitleMessage(c.getPlayer(), Text.of(TextColors.GRAY, "Your Turn"));
            }
        }
    }

    public Cell getSpawnCell() {
        return spawnCell;
    }

    public void checkAlive(){
        boolean temp = true;

        if(combatants.size() == 0){
            alive = false;
        } else {
            for (CombatantProfile combatantProfile : combatants) {
                if (combatantProfile.getCard().getHealth().getCurrent() <= 0) {
                    temp = false;
                } else {
                    temp = true;
                    break;
                }
            }
            alive = temp;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Vector3d getSpawn() {
        return spawn;
    }

    public void broadcastEndTurnMessage(Text s) {
        for(CombatantProfile c: combatants){
            if(c.isPlayer()){
                Messager.sendTitleMessage(c.getPlayer(), s);
            }
        }
    }

    public List<CombatantProfile> getCombatants() {
        return combatants;
    }

    public boolean isEmptyOfPlayers() {
        for(CombatantProfile profile: combatants){
            if(profile.isPlayer()){
                return false;
            }
        }
        return true;
    }

    public boolean isPlayerCombatant(Player player) {
        for(CombatantProfile p: combatants){
            if(p.isPlayer() && p.getUUID().compareTo(player.getUniqueId()) == 0){
                return true;
            }
        }
        return false;
    }

    public boolean isCombatant(UUID uuid){
        for(CombatantProfile p: combatants){
            if(p.getUUID().compareTo(uuid) == 0){
                return true;
            }
        }
        return false;
    }

    public void playerQuit(Player player) {
        for(CombatantProfile c: combatants){
            if(c.isPlayer() && c.getUUID().compareTo(player.getUniqueId()) == 0){
                combatants.remove(c);
            }
        }
    }

    public void drawCard() {
        for(CombatantProfile c: combatants){
            if(c.drawCard()){
                if(c.isPlayer()){
                    //This will update the player's hand
                    (new HotbarHand((CombatantProfilePlayer)c)).setHotbar(c.getPlayer());
                }
            }
        }
    }

    public void setSpawn(Cell spawn) {
        this.spawnCell = spawn;
        setSpawn(spawn.getCenterCeiling().clone().add(0, 10, 0));
    }

    private void setSpawn(Vector3d spawn){
        this.spawn = spawn;
    }

    public boolean voteToEndTurn(UUID uniqueId) {
        for(UUID uuid: votesToEndTurn){
            if(uuid.compareTo(uniqueId) == 0){
                Optional<Player> player = PlayerUtils.getPlayer(uniqueId);
                if(player.isPresent()){
                    Messager.sendMessage(player.get(), Text.of(TextColors.RED, "You've already voted to end your turn"), Messager.Prefix.DUEL);
                    return votesToEndTurn.size() == combatants.size();
                }
            }
        }

        votesToEndTurn.add(uniqueId);
        return votesToEndTurn.size() == combatants.size();
    }

    public void clearVotes() {
        votesToEndTurn.clear();
    }
}
