package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3i;

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

        //StatBase hOne = one.getStats().getStat(StatIDs.HEALTH).get();
        //StatBase hTwo = two.getStats().getStat(StatIDs.HEALTH).get();
        StatBase aOne = one.getStats().getStat(StatIDs.ATTACK).get();
        StatBase aTwo = two.getStats().getStat(StatIDs.ATTACK).get();

        //hOne.subtract(aTwo.getCurrent());
        //hTwo.subtract(aOne.getCurrent());

        BattleResult.BattleResultBuilder result = BattleResult.builder();

        if(aOne.getCurrent() > aTwo.getCurrent()){
            result.setVictor(PlayerUtils.getCombatProfilePlayer(one.getOwner()).get(), one);
            result.setLoser(PlayerUtils.getCombatProfilePlayer(two.getOwner()).get(), two);

            two.cardEliminated();
        } else if(aOne.getCurrent() < aTwo.getCurrent()){
            result.setLoser(PlayerUtils.getCombatProfilePlayer(one.getOwner()).get(), one);
            result.setVictor(PlayerUtils.getCombatProfilePlayer(two.getOwner()).get(), two);

            one.cardEliminated();
        }

        return result.build();
    }
}
