package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;

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
        teams.get(0).setSpawn(grid.getRowFirst().getCenterCell());
        teams.get(1).setSpawn(grid.getRowLast().getCenterCell());
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
    public void battle(Cell first, Cell second) {
        CardBase one = first.getOccupyingCard(), two = second.getOccupyingCard();

        StatBase hOne = one.getStats().getStat(StatIDs.HEALTH).get();
        StatBase hTwo = two.getStats().getStat(StatIDs.HEALTH).get();
        StatBase aOne = one.getStats().getStat(StatIDs.ATTACK).get();
        StatBase aTwo = two.getStats().getStat(StatIDs.ATTACK).get();

        hOne.subtract(aTwo.getCurrent());
        hTwo.subtract(aOne.getCurrent());

        if(hOne.getCurrent() <= 0){
            one.cardEliminated();
        }
        if(hTwo.getCurrent() <= 0){
            two.cardEliminated();
        }
    }
}
