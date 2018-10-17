package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseAvatar;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
    protected void generateSpawnPoints() {
        //We need 2
        Grid grid = arena.getGrid();
        Cell firstSpawn = grid.getRowFirst().getCenterCell();
        Cell secondSpawn = grid.getRowLast().getCenterCell();

        teams.get(0).setSpawn(firstSpawn);
        teams.get(1).setSpawn(secondSpawn);

    }

    @Override
    public String getName() {
        return "Duel";
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public BattleResult battle(Cell attacker, Cell defender) {
        CardBase one = attacker.getOccupyingCard(), two = defender.getOccupyingCard();

        BattleResult.BattleResultBuilder result = BattleResult.builder();

        Sponge.getEventManager().post(new DuelEvent.BattleBeginning(ExcelsiorSponge.getServerCause(), one, two));

        CombatantProfilePlayer cppOne = DuelUtils.getCombatProfilePlayer(one.getOwner()).get();
        CombatantProfilePlayer cppTwo = DuelUtils.getCombatProfilePlayer(two.getOwner()).get();

        double statOne = one.getPower().getCurrent();
        double statTwo = two.getCardPosition() == CardBase.CardPosition.ATTACK ? two.getPower().getCurrent() : two.getHealth().getCurrent();

        if(one.getCardFacePosition() == CardBase.CardFacePosition.FACE_DOWN){
            one.flipCard();
        }
        if(two.getCardFacePosition() == CardBase.CardFacePosition.FACE_DOWN){
            two.flipCard();
        }

        if(statOne > statTwo){
            /**
             * ===== Pre damage events and dealing damage =====
             */

            DuelEvent.CombatantDealDamage.Pre deal = new DuelEvent.CombatantDealDamage.Pre(ExcelsiorSponge.getServerCause(),
                    cppOne, cppTwo);

            DuelEvent.CombatantDealtDamage.Pre dealt = new DuelEvent.CombatantDealtDamage.Pre(ExcelsiorSponge.getServerCause(),
                    cppOne, cppTwo);

            Sponge.getEventManager().post(deal);
            Sponge.getEventManager().post(dealt);

            if(!deal.isCancelled() && !dealt.isCancelled()){
                result.setVictor(cppOne, one);
                result.setLoser(cppTwo, two);
            }

            /**
             * ===== Post damage events and destroy card =====
             */

            DuelEvent.CombatantDealDamage.Post dealP = new DuelEvent.CombatantDealDamage.Post(ExcelsiorSponge.getServerCause(),
                    cppOne, cppTwo);

            DuelEvent.CombatantDealtDamage.Post dealtP = new DuelEvent.CombatantDealtDamage.Post(ExcelsiorSponge.getServerCause(),
                    cppOne, cppTwo);

            Sponge.getEventManager().post(dealP);
            Sponge.getEventManager().post(dealtP);

            if(!dealP.isCancelled() && !dealtP.isCancelled()){

                if(!(two instanceof CardBaseAvatar)){
                    double difference = statOne - statTwo;

                    cppTwo.getCard().subtractHealth(difference);
                    Messager.sendMessage(cppOne.getPlayer(),
                            Text.of(TextColors.GREEN, "Dealt " + difference + " damage to enemy avatar"), Messager.Prefix.DUEL);

                    if(two.getCardPosition() == CardBase.CardPosition.ATTACK) {
                        two.cardEliminated();
                        result.setDefenderDestroyed(true);
                    }
                } else {
                    two.subtractHealth(statOne);
                }
            }



        } else if(statOne < statTwo){
            /**
             * ===== Pre damage events and dealing damage =====
             */

            DuelEvent.CombatantDealDamage.Pre deal = new DuelEvent.CombatantDealDamage.Pre(ExcelsiorSponge.getServerCause(),
                    cppTwo, cppOne);

            DuelEvent.CombatantDealtDamage.Pre dealt = new DuelEvent.CombatantDealtDamage.Pre(ExcelsiorSponge.getServerCause(),
                    cppTwo, cppOne);

            Sponge.getEventManager().post(deal);
            Sponge.getEventManager().post(dealt);

            if(!deal.isCancelled() && !dealt.isCancelled()){
                result.setLoser(cppOne, one);
                result.setVictor(cppTwo, two);
            }

            /**
             * ===== Post damage events and destroy card =====
             */

            DuelEvent.CombatantDealDamage.Post dealP = new DuelEvent.CombatantDealDamage.Post(ExcelsiorSponge.getServerCause(),
                    cppTwo, cppOne);

            DuelEvent.CombatantDealtDamage.Post dealtP = new DuelEvent.CombatantDealtDamage.Post(ExcelsiorSponge.getServerCause(),
                    cppTwo, cppOne);

            Sponge.getEventManager().post(dealP);
            Sponge.getEventManager().post(dealtP);

            if(!dealP.isCancelled() && !dealtP.isCancelled()){

                double difference = statTwo - statOne;
                cppOne.getCard().subtractHealth(difference);
                Messager.sendMessage(cppTwo.getPlayer(),
                        Text.of(TextColors.GREEN, "Dealt " + difference + " damage to enemy avatar"), Messager.Prefix.DUEL);

                if(two.getCardPosition() == CardBase.CardPosition.ATTACK){
                    one.cardEliminated();
                }
            }
        }

        BattleResult r = result.build();

        Sponge.getEventManager().post(new DuelEvent.BattleEnd(ExcelsiorSponge.getServerCause(), r));

        return r;
    }
}
