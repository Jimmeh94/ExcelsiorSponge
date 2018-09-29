package com.excelsiormc.excelsiorsponge.utils;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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

    public static void sendCardToChat(CardBase card, Player displayTo){
        Text message = Text.builder("[" + card.getName().toPlain() + "]").color(TextColors.GOLD)
                .onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                    @Override
                    public void accept(CommandSource commandSource) {
                        card.displayStats(displayTo);
                    }
                })).build();
        Messager.sendMessage(displayTo, message);
    }

    public static Team getTeam(UUID combatant){
        return ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(combatant).get().getGamemode().getTeamWithCombatant(combatant);
    }

    public static Optional<Arena> findArenaWithPlayer(Player player){
        return ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player);
    }

}
