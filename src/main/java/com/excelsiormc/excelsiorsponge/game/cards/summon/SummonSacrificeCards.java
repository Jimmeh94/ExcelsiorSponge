package com.excelsiormc.excelsiorsponge.game.cards.summon;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.inventory.SummonSacrificeInventory;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class SummonSacrificeCards extends SummonType {

    protected int amountNeeded;
    protected SummonSacrificeInventory inventory;

    public SummonSacrificeCards(int amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    @Override
    public boolean canSummon() {
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
        List<CardBase> cards = DuelUtils.getArena(owner.getOwner()).get().getGrid().getActiveCardsOnFieldFor(owner.getOwner());

        double total = 0;
        for(CardBase cardBase: cards){
            if(cardBase instanceof CardBaseMonster){
                total++;
            }
        }

        if(total >= amountNeeded){
            return true;
        } else {
            Messager.sendMessage(cpp.getPlayer(), Text.of(TextColors.RED, "You don't have enough cards summoned to summon this"), Messager.Prefix.DUEL);
            return false;
        }
    }

    @Override
    public void summon() {
        inventory = new SummonSacrificeInventory(owner, amountNeeded);
    }

    public void resetInventory(){
        inventory = null;
    }

    public void finishSummon(){
        if(layCardOnField()) {
            Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(PlayerUtils.getPlayer(owner.getOwner()).get());
        }
    }
}
