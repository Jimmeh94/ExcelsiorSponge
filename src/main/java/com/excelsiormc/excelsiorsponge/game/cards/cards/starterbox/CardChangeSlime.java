package com.excelsiormc.excelsiorsponge.game.cards.cards.starterbox;

import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovementNormal;
import com.excelsiormc.excelsiorsponge.game.cards.movement.filters.FilterIncludeEnemyEmptyCell;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonTypeEnergy;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CardChangeSlime extends CardBaseMonster {

    public CardChangeSlime(UUID owner, CardDescriptor descriptor) {
        super(owner, descriptor, new StatPower(descriptor.getBasePower()), new StatHealth(descriptor.getBaseHealth()),
                new CardMovementNormal(1, new FilterIncludeEnemyEmptyCell()),
                new SummonTypeEnergy(1));
    }

    @Override
    protected void summoned(){
        List<CardBase> cards = DuelUtils.filterCardsForMonsters(DuelUtils.getAllActiveCards(getOwner()));
        cards.remove(this);
        if(cards.size() > 0){
            CardBase chosen = cards.get((new Random()).nextInt(cards.size()));
            this.descriptor.name = chosen.getDescriptor().getName();
            this.health = new StatHealth(chosen.getHealth());
            this.power = new StatPower(chosen.getPower());
            this.faceupItem = chosen.getFaceupItem();
            updateLore();

            if(cardFacePosition == CardFacePosition.FACE_UP){
                stand.setHelmet(faceupItem);
                stand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
            }

            if(isOwnerPlayer()){
                Messager.sendMessage(PlayerUtils.getPlayer(getOwner()).get(), Text.builder().append(Text.of(TextColors.GREEN,
                        "Change Slime has adopted the shape and stats of ")).append(getName()).build(), Messager.Prefix.DUEL);
            }
        }
    }
}
