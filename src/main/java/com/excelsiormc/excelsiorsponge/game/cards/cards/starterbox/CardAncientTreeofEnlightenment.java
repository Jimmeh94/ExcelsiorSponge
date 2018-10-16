package com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseTrap;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonTypeEnergy;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public class CardAncientTreeofEnlightenment extends CardBaseMonster {

    public CardAncientTreeofEnlightenment(UUID owner, CardDescriptor descriptor) {
        super(owner, descriptor, new StatPower(descriptor.getBasePower()), new StatHealth(descriptor.getBaseHealth()),
                new CardMovementNormal(1, new FilterIncludeEnemyEmptyCell()),
                new SummonTypeEnergy(3));
    }

    @Override
    protected void summoned(){
        Sponge.getEventManager().registerListeners(ExcelsiorSponge.INSTANCE, this);
    }

    @Override
    public void remove(){
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onCardPlaced(DuelEvent.CardEvent.CardPlacePre event){
        Arena arena = DuelUtils.getArena(getOwner()).get();

        if(arena.getGrid().containsCard(event.getCard())){
            if(event.getCard() instanceof CardBaseTrap && !arena.getGamemode().isOnSameTeam(getOwner(), event.getCard().getOwner())){
                if(getCardFacePosition() == CardFacePosition.FACE_UP && getCardPosition() == CardPosition.DEFENSE) {
                    event.setCancelled(true);
                    arena.broadcastMessage(Text.builder().append(getName())
                            .append(Text.of(TextColors.GRAY, " stopped a trap card from being played: "))
                            .append(event.getCard().getName())
                            .build(), Messager.Prefix.DUEL);
                }
            }
        }
    }
}
