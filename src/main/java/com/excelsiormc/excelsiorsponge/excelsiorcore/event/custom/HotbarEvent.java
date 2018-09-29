package com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.event.cause.Cause;

public abstract class HotbarEvent extends CustomEvent {

    public HotbarEvent(Cause cause) {
        super(cause);
    }

    public static class HotbarChangedEvent extends HotbarEvent {

        private PlayerBase player;
        private Hotbar changedTo, changedFrom;

        public HotbarChangedEvent(Cause cause, PlayerBase player, Hotbar changedTo, Hotbar changedFrom) {
            super(cause);
            this.player = player;
            this.changedTo = changedTo;
            this.changedFrom = changedFrom;
        }

        public PlayerBase getPlayer() {
            return player;
        }

        public Hotbar getChangedTo() {
            return changedTo;
        }

        public Hotbar getChangedFrom() {
            return changedFrom;
        }
    }

}
