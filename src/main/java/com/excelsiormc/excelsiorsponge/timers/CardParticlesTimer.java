package com.excelsiormc.excelsiorsponge.timers;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.ParticleData;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.ParticlePlayer;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class CardParticlesTimer extends AbstractTimer {

    private Map<CardBase, ParticleData> cards;

    public CardParticlesTimer(long interval) {
        super(interval);

        cards = new HashMap<>();
        Sponge.getEventManager().registerListeners(ExcelsiorSponge.INSTANCE, this);
    }

    public void addCard(CardBase card, ParticleData data){
        if(cards.containsKey(card)){
           cards.remove(card);
        }
        cards.put(card, data);
    }

    public void remove(CardBase card){
        cards.remove(card);
    }

    @Override
    protected void runTask() {
        for(Map.Entry<CardBase, ParticleData> entry: cards.entrySet()){
            entry.getValue().setDisplayAt(entry.getKey().getCurrentCell().getCenterCeiling());
            ParticlePlayer.display(entry.getValue());
        }
    }

    @Listener
    public void onRemove(DuelEvent.CardEvent.CardDestroyed event){
        remove(event.getCard());
    }
}
