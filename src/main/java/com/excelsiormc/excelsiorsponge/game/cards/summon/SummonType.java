package com.excelsiormc.excelsiorsponge.game.cards.summon;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public abstract class SummonType {

    protected CardBase owner;

    public abstract boolean canSummon();

    public abstract void summon();

    public void setOwner(CardBase owner) {
        this.owner = owner;
    }

    protected boolean layCardOnField(){
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
        Player player = cpp.getPlayer();

        //Lay card on field
        Optional<Cell> currentAim = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get().getCurrentAim();

        if (currentAim.isPresent() && currentAim.get().isAvailable()) {
            if(DuelUtils.getTeam(player.getUniqueId()).isPlaceable(currentAim.get())) {
                currentAim.get().setOccupyingCard(owner, true);
                cpp.getHand().removeCard(owner);
                cpp.getUserPlayer().getCurrentHotbar().setHotbar(player);

                if(owner.isOwnerPlayer()) {
                    DuelUtils.getTeam(owner.getOwner()).eraseAsPlaceableRows(PlayerUtils.getPlayer(owner.getOwner()).get());
                }

                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(PlayerUtils.getPlayer(owner.getOwner()).get());
                return true;
            }
        }
        return false;
    }

    protected boolean isCellEmpty(CombatantProfilePlayer cpp){
        return cpp.getCurrentAim().isPresent() && cpp.getCurrentAim().get().isAvailable();
    }
}
