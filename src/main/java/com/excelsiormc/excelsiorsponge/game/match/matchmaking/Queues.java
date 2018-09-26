package com.excelsiormc.excelsiorsponge.game.match.matchmaking;

import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemodes;

public enum Queues {

    DUEL(new Queue(Gamemodes.DUEL));

    private Queue queue;

    Queues(Queue queue) {
        this.queue = queue;
    }

    public Queue getQueue() {
        return queue;
    }
}
