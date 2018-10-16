package com.excelsiormc.excelsiorsponge.events.custom;

import com.excelsiormc.excelsiorsponge.excelsiorcore.event.custom.CustomEvent;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import org.spongepowered.api.event.Cancellable;
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

    public static abstract class CardEvent extends DuelEvent{

        private CardBase card;

        public CardEvent(Cause cause, CardBase card) {
            super(cause);

            this.card = card;
        }

        public CardBase getCard() {
            return card;
        }

        public static class CardPlacePre extends CardEvent implements Cancellable{

            private boolean cancelled;

            public CardPlacePre(Cause cause, CardBase card) {
                super(cause, card);
            }

            @Override
            public boolean isCancelled() {
                return cancelled;
            }

            @Override
            public void setCancelled(boolean cancel) {
                this.cancelled = cancel;
            }
        }

        public static class CardPlacePost extends CardEvent {

            public CardPlacePost(Cause cause, CardBase card) {
                super(cause, card);
            }
        }

        public static class CardDestroyed extends CardEvent {

            public CardDestroyed(Cause cause, CardBase card) {
                super(cause, card);
            }
        }

        public static class CardMoved extends CardEvent {

            private Cell old, current;

            public CardMoved(Cause cause, CardBase card, Cell old, Cell current) {
                super(cause, card);
                this.old = old;
                this.current = current;
            }

            public Cell getOld() {
                return old;
            }

            public Cell getCurrent() {
                return current;
            }
        }

        public static class CardFlipped extends CardEvent {

            public CardFlipped(Cause cause, CardBase card) {
                super(cause, card);
            }
        }

    }



    public static class AimUpdated extends DuelEvent {

        private CombatantProfilePlayer cpp;

        public AimUpdated(Cause cause, CombatantProfilePlayer cpp) {
            super(cause);

            this.cpp = cpp;
        }

        public CombatantProfilePlayer getCombatPlayerProfile() {
            return cpp;
        }
    }

    public abstract static class CombatantDealtDamage extends DuelEvent {

        private CombatantProfile dealer, receiver;

        public CombatantDealtDamage(Cause cause, CombatantProfile dealer, CombatantProfile receiver) {
            super(cause);
            this.dealer = dealer;
            this.receiver = receiver;
        }

        public CombatantProfile getDealer() {
            return dealer;
        }

        public CombatantProfile getReceiver() {
            return receiver;
        }

        public static class Pre extends CombatantDealtDamage implements Cancellable{

            private boolean cancelled;

            public Pre(Cause cause, CombatantProfile dealer, CombatantProfile receiver) {
                super(cause, dealer, receiver);
            }

            @Override
            public boolean isCancelled() {
                return cancelled;
            }

            @Override
            public void setCancelled(boolean cancel) {
                this.cancelled = cancel;
            }
        }

        public static class Post extends CombatantDealtDamage implements Cancellable{

            private boolean cancelled;

            public Post(Cause cause, CombatantProfile dealer, CombatantProfile receiver) {
                super(cause, dealer, receiver);
            }

            @Override
            public boolean isCancelled() {
                return cancelled;
            }

            @Override
            public void setCancelled(boolean cancel) {
                this.cancelled = cancel;
            }
        }
    }

    public abstract static class CombatantDealDamage extends DuelEvent {

        private CombatantProfile dealer, receiver;

        public CombatantDealDamage(Cause cause, CombatantProfile dealer, CombatantProfile receiver) {
            super(cause);
            this.dealer = dealer;
            this.receiver = receiver;
        }

        public CombatantProfile getDealer() {
            return dealer;
        }

        public CombatantProfile getReceiver() {
            return receiver;
        }

        public static class Pre extends CombatantDealDamage implements Cancellable {

            private boolean cancelled;

            public Pre(Cause cause, CombatantProfile dealer, CombatantProfile receiver) {
                super(cause, dealer, receiver);
            }

            @Override
            public boolean isCancelled() {
                return cancelled;
            }

            @Override
            public void setCancelled(boolean cancel) {
                this.cancelled = cancel;
            }
        }

        public static class Post extends CombatantDealDamage implements Cancellable{

            private boolean cancelled;

            public Post(Cause cause, CombatantProfile dealer, CombatantProfile receiver) {
                super(cause, dealer, receiver);
            }

            @Override
            public boolean isCancelled() {
                return cancelled;
            }

            @Override
            public void setCancelled(boolean cancel) {
                this.cancelled = cancel;
            }
        }
    }

    public static class BattleBeginning extends CustomEvent {

        private CardBase one, two;

        public BattleBeginning(Cause cause, CardBase one, CardBase two) {
            super(cause);
            this.one = one;
            this.two = two;
        }

        public CardBase getOne() {
            return one;
        }

        public CardBase getTwo() {
            return two;
        }
    }

    public static class BattleEnd extends CustomEvent {

        private BattleResult result;

        public BattleEnd(Cause cause, BattleResult result) {
            super(cause);
            this.result = result;
        }

        public BattleResult getResult() {
            return result;
        }
    }
}
