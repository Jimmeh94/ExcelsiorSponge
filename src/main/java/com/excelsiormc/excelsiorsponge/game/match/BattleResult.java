package com.excelsiormc.excelsiorsponge.game.match;

import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

import java.util.Optional;

public class BattleResult {

    public static BattleResultBuilder builder(){return new BattleResultBuilder();}

    private Optional<CombatantProfile> victor, loser;

    private Optional<CardBase> victorCard, loserCard;

    protected BattleResult(Optional<CombatantProfile> victor, Optional<CombatantProfile> loser, Optional<CardBase> victorCard, Optional<CardBase> loserCard) {
        this.victor = victor;
        this.loser = loser;
        this.victorCard = victorCard;
        this.loserCard = loserCard;
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

        public BattleResultBuilder setVictor(CombatantProfile cp, CardBase card){
            this.victor = Optional.of(cp);
            this.victorCard = Optional.of(card);
            return this;
        }

        public BattleResultBuilder setLoser(CombatantProfile cp, CardBase card){
            this.loser = Optional.of(cp);
            this.loserCard = Optional.of(card);
            return this;
        }

        public BattleResult build(){
            return new BattleResult(victor, loser, victorCard, loserCard);
        }
    }
}
