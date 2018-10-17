package com.excelsiormc.excelsiorsponge.game.cards.components.actives.cards;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.ParticleData;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ActiveHide extends ActiveCard{

    private final int turnLifetime;
    private final String cardExtraInfoID = "ActiveHideCardID";
    private int count = 0;

    public ActiveHide(CardBase card, int turnLifetime) {
        super(card, Text.of(TextColors.YELLOW, "Hide"));

        this.turnLifetime = turnLifetime;
    }

    @Override
    public void action() {
        Sponge.getEventManager().registerListeners(ExcelsiorSponge.INSTANCE, this);
        //start particle runnable
        getCard().remove();

        Arena arena = DuelUtils.getArena(card.getOwner()).get();
        for(Team team: arena.getGamemode().getTeams()){
            if(team.isCombatant(card.getOwner())){
                continue;
            } else {
                Text text = Text.builder().append(Text.of(TextColors.RED, "Enemy's ")).append(card.getName())
                        .append(Text.of(TextColors.RED, " is now hidden for " + turnLifetime
                                + " turns (will end early if they attack)")).build();
                for(CombatantProfile c: team.getCombatants()){
                    if(c.isPlayer()){
                        Player player = c.getPlayer();
                        card.getCurrentCell().eraseClient(player);
                        Messager.sendMessage(player, text, Messager.Prefix.DUEL);
                    }
                }
            }
        }

        if(getCard().isOwnerPlayer()) {
            ParticleData.Builder builder = ParticleData.builder();
            builder.addViewer(PlayerUtils.getUserPlayer(card.getOwner()).get())
                    .center(card.getCurrentCell().getCenterCeiling())
                    .quantity(15)
                    .offsets(0.3, 0.3, 0.3)
                    .type(ParticleTypes.CLOUD);
            ExcelsiorSponge.INSTANCE.getCardParticlesTimer().addCard(card, builder.build());

            Messager.sendMessage(PlayerUtils.getPlayer(card.getOwner()).get(), Text.builder().append(card.getName())
                    .append(Text.of(TextColors.GREEN, " is now hidden for " + turnLifetime + " turns (will end early if you attack)")).build(),
                    Messager.Prefix.DUEL);
        }

        card.addExtraDisplayInfo(cardExtraInfoID, Text.builder().append(Text.of(TextColors.YELLOW, "Hidden: "))
                .append(Text.of(TextColors.GRAY, String.valueOf(turnLifetime - count) + " turns")).build());
        card.addKey(CardBase.CardKeys.DONT_DRAW_CELL_FOR_ENEMY);
    }

    @Listener
    public void onTurnEnd(DuelEvent.EndTurn event){
        if(turnLifetime != -1) {
            if (event.getTeam().isCombatant(getCard().getOwner())) {
                count++;
                if(turnLifetime == count){
                    end();
                } else {
                    card.removeExtraDisplayInfo(cardExtraInfoID);
                    card.addExtraDisplayInfo(cardExtraInfoID, Text.builder().append(Text.of(TextColors.YELLOW, "Hidden: "))
                            .append(Text.of(TextColors.GRAY, String.valueOf(turnLifetime - count) + " turns")).build());
                }
            }
        }
    }

    @Listener
    public void onAttack(DuelEvent.CombatantDealtDamage.Pre event){
        if(event.getDealer().getUUID().compareTo(card.getOwner()) == 0) {
            end();
        }
    }

    private void end(){
        Sponge.getEventManager().unregisterListeners(this);
        getCard().spawn();
        card.removeKey(CardBase.CardKeys.DONT_DRAW_CELL_FOR_ENEMY);
        ExcelsiorSponge.INSTANCE.getCardParticlesTimer().remove(card);
        Messager.sendMessage(PlayerUtils.getPlayer(card.getOwner()).get(), Text.builder().append(card.getName())
                        .append(Text.of(TextColors.RED, " is no longer hidden!")).build(), Messager.Prefix.DUEL);

        Arena arena = DuelUtils.getArena(card.getOwner()).get();
        for(Team team: arena.getGamemode().getTeams()){
            if(team.isCombatant(card.getOwner())){
                continue;
            } else {
                Text text = Text.builder().append(Text.of(TextColors.GREEN, "Enemy's ")).append(card.getName())
                        .append(Text.of(TextColors.GREEN, " is no longer hidden!")).build();
                for(CombatantProfile c: team.getCombatants()){
                    if(c.isPlayer()){
                        Player player = c.getPlayer();
                        card.getCurrentCell().drawCustomEnemyNoCurrentThreat(player);
                        Messager.sendMessage(player, text, Messager.Prefix.DUEL);
                    }
                }
            }
        }
    }
}
