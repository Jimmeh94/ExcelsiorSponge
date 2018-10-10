package com.excelsiormc.excelsiorsponge.game.cards.summon;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.inventory.SummonSacrificeInventory;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class SummonSacrificePower extends SummonType {

    protected double amountNeeded;

    public SummonSacrificePower(double amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    @Override
    public boolean canSummon() {
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
        List<CardBase> cards = DuelUtils.getArena(owner.getOwner()).get().getGrid().getActiveCardsOnFieldFor(owner.getOwner());

        double totalPower = 0;
        for(CardBase cardBase: cards){
            totalPower += cardBase.getPower().getCurrent();
        }

        if(totalPower >= amountNeeded){
            return true;
        } else {
            Messager.sendMessage(cpp.getPlayer(), Text.of(TextColors.RED, "You don't have enough cards summoned with a total power needed to summon this"), Messager.Prefix.DUEL);
            return false;
        }
    }

    @Override
    public void summon() {
        new SummonSacrificeInventory(owner);
    }

    public void finishSummon(){
        if(layCardOnField()) {
            Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(PlayerUtils.getPlayer(owner.getOwner()).get());
        }
    }
}
