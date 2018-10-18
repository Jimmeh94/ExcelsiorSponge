package com.excelsiormc.excelsiorsponge.game.match;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

import java.util.Optional;

public class BattleResult {

    public static BattleResultBuilder builder(){return new BattleResultBuilder();}

    private Optional<CombatantProfile> victor, loser;

    private Optional<CardBase> victorCard, loserCard;

    private boolean defenderDestroyed = false;

    private boolean draw;

    protected BattleResult(Optional<CombatantProfile> victor, Optional<CombatantProfile> loser,
                           Optional<CardBase> victorCard, Optional<CardBase> loserCard, boolean defenderDestroyed, boolean draw) {
        this.victor = victor;
        this.loser = loser;
        this.victorCard = victorCard;
        this.loserCard = loserCard;
        this.defenderDestroyed = defenderDestroyed;
        this.draw = draw;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isDefenderDestroyed() {
        return defenderDestroyed;
    }

    public Optional<CombatantProfile> getVictor() {
        return victor;
    }

    public Optional<CombatantProfile> getLoser() {
        return loser;
    }

    public Optional<CardBase> getVictorCard() {
        return victorCard;
    }

    public Optional<CardBase> getLoserCard() {
        return loserCard;
    }

    public static class BattleResultBuilder{

        private Optional<CombatantProfile> victor = Optional.empty(), loser = Optional.empty();

        private Optional<CardBase> victorCard = Optional.empty(), loserCard = Optional.empty();

        private boolean defenderDestroyed = false;

        private boolean draw = false;

        public BattleResultBuilder setVictor(CombatantProfile cp, CardBase card){
            this.victor = Optional.of(cp);
            this.victorCard = Optional.of(card);
            return this;
        }

        public BattleResultBuilder setDraw(){
            draw = true;
            return this;
        }

        public BattleResultBuilder setLoser(CombatantProfile cp, CardBase card){
            this.loser = Optional.of(cp);
            this.loserCard = Optional.of(card);
            return this;
        }

        public BattleResultBuilder setDefenderDestroyed(boolean b){
            this.defenderDestroyed = b;
            return this;
        }

        public BattleResult build(){
            return new BattleResult(victor, loser, victorCard, loserCard, defenderDestroyed, draw);
        }
    }
}
