package com.excelsiormc.excelsiorsponge.game.match.matchmaking;

import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemodes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public enum Queues {

    DUEL(new Queue(Gamemodes.DUEL), Text.of(TextColors.GREEN, "You joined the queue for duels!"), Text.of(TextColors.RED, "You have left the queue for duels!"));

    private Queue queue;
    private Text joinMessage, leaveMessage;

    Queues(Queue queue, Text joinMessage, Text leaveMessage) {
        this.queue = queue;
        this.joinMessage = joinMessage;
        this.leaveMessage = leaveMessage;
    }

    public Text getLeaveMessage() {
        return leaveMessage;
    }

    public Text getJoinMessage() {
        return joinMessage;
    }

    public Queue getQueue() {
        return queue;
    }
}
