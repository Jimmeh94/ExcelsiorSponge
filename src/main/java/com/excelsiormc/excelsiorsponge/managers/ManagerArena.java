package com.excelsiormc.excelsiorsponge.managers;


import com.excelsiormc.excelsiorcore.services.Manager;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

public class ManagerArena extends Manager<Arena> {

    public void updatePlayersAim() {
        for(Arena a: objects){
            if(a.isInUse()) {
                a.updatePlayersAim();
            }
        }
    }

    public Optional<Arena> getAvailableArena() {
        for(Arena a: objects){
            if(!a.isInUse()){
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }

    public void checkPlayer(Player player) {
        for(Arena arena: objects){
            if(arena.isInUse()){
               if(arena.isPlayerCombatant(player)){
                    arena.playerQuit(player);
               }
            }
        }
    }

    public Optional<Arena> findArenaWithCombatant(UUID uuid){
        for(Arena arena: objects){
            if(arena.isCombatant(uuid)){
                return Optional.of(arena);
            }
        }
        return Optional.empty();
    }

    public Optional<Arena> findArenaWithPlayer(Player player) {
        for(Arena arena: objects){
            if(arena.isPlayerCombatant(player)){
                return Optional.of(arena);
            }
        }
        return Optional.empty();
    }
}
