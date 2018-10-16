package com.excelsiormc.excelsiorsponge.game.cards.summon;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptors;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class SummonFusion extends SummonType {

    private CardDescriptors[] needed;

    public SummonFusion(CardDescriptors... needed) {
        this.needed = needed;
    }

    @Override
    public boolean canSummon() {
        List<CardBase> cards = DuelUtils.getCombatantsPlayedCards(owner.getOwner());
        boolean has = false;
        for(CardDescriptors descriptor: needed){
            has = false;
            for(CardBase c: cards){
                if(c.getName().toPlain().equalsIgnoreCase(descriptor.getCardDescriptor().getName().toPlain())){
                    has = true;
                }
            }
            if(!has){
                if(owner.isOwnerPlayer()) {
                    String string = "";
                    for(int i = 0; i < needed.length; i++){
                        string += needed[i].getCardDescriptor().getName().toPlain();
                        if(i <= needed.length - 2){
                            string += ", ";
                        }
                    }
                    Messager.sendMessage(PlayerUtils.getPlayer(owner.getOwner()).get(),
                            Text.of(TextColors.RED, "You need to have these on the field to fuse: " + string), Messager.Prefix.DUEL);
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void summon() {
        List<CardBase> cards = DuelUtils.getCombatantsPlayedCards(owner.getOwner());
        for(CardDescriptors descriptor: needed){
            for(CardBase c: cards){
                if(c.getName().toPlain().equalsIgnoreCase(descriptor.getCardDescriptor().getName().toPlain())){
                    c.cardEliminated();
                    cards.remove(c);
                }
            }
        }
        layCardOnField();
    }
}
