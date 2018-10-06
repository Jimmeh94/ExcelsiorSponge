package com.excelsiormc.excelsiorsponge.game.cards.summon;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.InventoryUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class SummonTypeEnergy extends SummonType {

    private int cost;

    public SummonTypeEnergy(int cost) {
        this.cost = cost;
    }

    @Override
    public boolean canSummon() {
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
        if(cpp.getSummonEnergy() >= cost){
            return true;
        } else {
            Messager.sendMessage(cpp.getPlayer(), Text.of(TextColors.RED, "You don't have enough energy to summon this!", Messager.Prefix.DUEL));
            return false;
        }
    }

    @Override
    public void summon() {
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(owner.getOwner()).get();
        Player player = cpp.getPlayer();

        if(ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player).get().getGamemode().getStage()
                != Gamemode.Stage.IN_GAME){
            return;
        }

        int index = InventoryUtils.getHeldItemSlot(player, HandTypes.MAIN_HAND).get().getValue();
        //Lay card on field
        Optional<Cell> currentAim = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get().getCurrentAim();

        if (currentAim.isPresent() && currentAim.get().isAvailable() && cpp.getHand().hasCardAt(index)) {

            if(DuelUtils.getTeam(player.getUniqueId()).isPlaceable(currentAim.get())) {
                currentAim.get().setOccupyingCard(cpp.getHand().getCard(index), true);

                Optional<Slot> op = InventoryUtils.getSlot(index, player);
                if (op.isPresent()) {
                    op.get().clear();
                }
                cpp.getUserPlayer().getCurrentHotbar().setHotbar(player);
            }
        }
    }
}
