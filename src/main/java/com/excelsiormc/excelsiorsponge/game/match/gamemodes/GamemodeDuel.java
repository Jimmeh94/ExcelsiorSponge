package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.Sponge;

public class GamemodeDuel extends Gamemode {


    public GamemodeDuel(String world) {
        //Time limit in seconds, each turn's time limit in seconds
        super(60 * 15, 60 * 1, world);
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void endingGame() {

    }

    @Override
    protected void startingGame() {

    }

    @Override
    public void battle(Cell first, Cell second) {
        CardBase one = first.getOccupyingCard(), two = second.getOccupyingCard();
        if(one instanceof CardBaseMonster && two instanceof CardBaseMonster){
            CardBaseMonster mOne = (CardBaseMonster) one, mTwo = (CardBaseMonster) two;
            StatBase hOne = mOne.getStats().getStat(StatIDs.HEALTH).get();
            StatBase hTwo = mTwo.getStats().getStat(StatIDs.HEALTH).get();
            StatBase aOne = mOne.getStats().getStat(StatIDs.ATTACK).get();
            StatBase aTwo = mTwo.getStats().getStat(StatIDs.ATTACK).get();

            hOne.subtract(aTwo.getCurrent());
            hTwo.subtract(aOne.getCurrent());

            if(hOne.getCurrent() <= 0){
                one.removeArmorStand();
                first.setAvailable(true);
            }
            if(hTwo.getCurrent() <= 0){
                two.removeArmorStand();
                second.setAvailable(true);
            }
        }
    }

    @Override
    public String getName() {
        return "Duel";
    }

    @Override
    public int getID() {
        return 0;
    }
}
