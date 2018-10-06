package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseCombatant;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.flowpowered.math.vector.Vector3i;
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
    protected void generateSpawnPoints() {
        //We need 2
        Grid grid = arena.getGrid();
        Cell firstSpawn = grid.getRowFirst().getCenterCell();
        Cell secondSpawn = grid.getRowLast().getCenterCell();

        teams.get(0).setSpawn(firstSpawn);
        teams.get(1).setSpawn(secondSpawn);

        Cell gridCenter = grid.getCenterCell();
        Vector3i direction;

        direction = LocationUtils.getDirection(firstSpawn.getCenter(), gridCenter.getCenter());
        teams.get(0).addPlaceableRow(grid.getHorizontalRow(firstSpawn).get());

        firstSpawn = grid.getCellInDirection(firstSpawn, direction.toDouble(), false).get();
        teams.get(0).addPlaceableRow(grid.getHorizontalRow(firstSpawn).get());


        direction = LocationUtils.getDirection(secondSpawn.getCenter(), gridCenter.getCenter());
        teams.get(1).addPlaceableRow(grid.getHorizontalRow(secondSpawn).get());

        secondSpawn = grid.getCellInDirection(secondSpawn, direction.toDouble(), false).get();
        teams.get(1).addPlaceableRow(grid.getHorizontalRow(secondSpawn).get());

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
    public BattleResult battle(Cell first, Cell second) {
        CardBase one = first.getOccupyingCard(), two = second.getOccupyingCard();

        double aOne = one.getPower().getCurrent();
        double aTwo = two.getPower().getCurrent();

        BattleResult.BattleResultBuilder result = BattleResult.builder();

        Sponge.getEventManager().post(new DuelEvent.BattleBeginning(ExcelsiorSponge.getServerCause(), one, two));

        CombatantProfilePlayer cppOne = DuelUtils.getCombatProfilePlayer(one.getOwner()).get();
        CombatantProfilePlayer cppTwo = DuelUtils.getCombatProfilePlayer(two.getOwner()).get();
        
        if(aOne > aTwo){
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

                if(!(two instanceof CardBaseCombatant)){
                    two.cardEliminated();
                } else {
                    two.subtractHealth(aOne);
                }
            }



        } else if(aOne < aTwo){
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
                one.cardEliminated();
            }
        }

        BattleResult r = result.build();

        Sponge.getEventManager().post(new DuelEvent.BattleEnd(ExcelsiorSponge.getServerCause(), r));

        return r;
    }
}
