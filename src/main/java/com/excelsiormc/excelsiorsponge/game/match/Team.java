package com.excelsiormc.excelsiorsponge.game.match;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel.HotbarHand;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Team {

    private List<CombatantProfile> combatants;
    private Vector3d spawn;

    public Team() {
        combatants = new CopyOnWriteArrayList<>();
    }

    public Team(CombatantProfile... combatants){
        this.combatants = new CopyOnWriteArrayList<>();
        this.combatants.addAll(Arrays.asList(combatants));
    }

    public void addCombatant(CombatantProfile profile){
        combatants.add(profile);
    }

    public void broadcastStartTurnMessage() {
        for(CombatantProfile c: combatants){
            if(c.isPlayer()){
                //Messager.sendMessage(c.getPlayer(), ChatColor.YELLOW + "It's your turn", Messager.Prefix.DUEL, true);
                Messager.sendTitleMessage(c.getPlayer(), Text.of(TextColors.GRAY, "Your Turn"));
            }
        }
    }

    public Vector3d getSpawn() {
        return spawn;
    }

    public void broadcastEndTurnMessage(Text s) {
        for(CombatantProfile c: combatants){
            if(c.isPlayer()){
                //Messager.sendMessage(Bukkit.getPlayer(c.getUUID()), s, Optional.of(Messager.Prefix.DUEL), true);
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
        setSpawn(spawn.getCenter().clone().add(0, 10, 0));
    }

    public void setSpawn(Vector3d spawn){
        this.spawn = spawn;
    }
}
