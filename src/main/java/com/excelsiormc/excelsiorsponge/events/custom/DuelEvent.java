package com.excelsiormc.excelsiorsponge.events.custom;

import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.CustomEvent;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import org.spongepowered.api.event.cause.Cause;

public class DuelEvent extends CustomEvent {

    public DuelEvent(Cause cause) {
        super(cause);
    }

    public static class BeginTurn extends DuelEvent{

        private Team team;

        public BeginTurn(Cause cause, Team team) {
            super(cause);
            this.team = team;
        }

        public Team getTeam() {
            return team;
        }
    }

    public static class EndTurn extends DuelEvent{

        private Team team;

        public EndTurn(Cause cause, Team team) {
            super(cause);
            this.team = team;
        }

        public Team getTeam() {
            return team;
        }
    }

    public static class CardPlaced extends DuelEvent {

        private CardBase card;

        public CardPlaced(Cause cause, CardBase card) {
            super(cause);
            this.card = card;
        }

        public CardBase getCard() {
            return card;
        }
    }

    public static class CardDestroyed extends DuelEvent {

        private CardBase card;

        public CardDestroyed(Cause cause, CardBase card) {
            super(cause);
            this.card = card;
        }

        public CardBase getCard() {
            return card;
        }
    }

    public static class CardMoved extends DuelEvent {

        private CardBase card;
        private Cell old, current;

        public CardMoved(Cause cause, CardBase card, Cell old, Cell current) {
            super(cause);
            this.card = card;
            this.old = old;
            this.current = current;
        }

        public CardBase getCard() {
            return card;
        }

        public Cell getOld() {
            return old;
        }

        public Cell getCurrent() {
            return current;
        }
    }
}
