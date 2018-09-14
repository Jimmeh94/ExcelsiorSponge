package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

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
    public String getName() {
        return "Duel";
    }

    @Override
    public int getID() {
        return 0;
    }
}
