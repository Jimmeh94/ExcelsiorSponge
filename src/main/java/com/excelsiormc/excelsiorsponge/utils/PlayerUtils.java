package com.excelsiormc.excelsiorsponge.utils;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerUtils {

    public static Optional<UserPlayer> getUserPlayer(Player player){
        return getUserPlayer(player.getUniqueId());
    }

    public static Optional<UserPlayer> getUserPlayer(UUID uuid){
        Optional<PlayerBase> temp = ExcelsiorCore.INSTANCE.getPlayerBaseManager().getPlayerBase(uuid);

        return temp.isPresent() ? Optional.of((UserPlayer)temp.get()) : Optional.empty();
    }

    public static Optional<CombatantProfilePlayer> getCombatProfilePlayer(UUID uuid){
        Optional<CombatantProfile> c = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(uuid).get().getCombatantProfile(uuid);

        return c.isPresent() && c.get().isPlayer() ? Optional.of((CombatantProfilePlayer)c.get()) : Optional.empty();
    }

}
