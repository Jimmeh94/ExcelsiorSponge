package com.excelsiormc.excelsiorsponge.game.cards.archetypes;

import com.excelsiormc.excelsiorsponge.game.cards.archetypes.archetypes.*;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public enum Archetypes {

    /**
     * Archetypes:
     * - Can choose up to 2 for your deck
     * - The main sign has passives and actives you can use
     * - The secondary sign has passives you can use
     * - The signs also determine which terrain is advantageous, neutral, and hindering
     */

    AQUARIUS(new Aquarius()),
    PISCES(new Pisces()),
    ARIES(new Aries()),
    TAURUS(new Taurus()),
    GEMINI(new Gemini()),
    CANCER(new Cancer()),
    LEO(new Leo()),
    VIRGO(new Virgo()),
    LIBRA(new Libra()),
    SCORPIO(new Scorpio()),
    SAGITTARIUS(new Sagittarius()),
    CAPRICORN(new Capricorn());

    private Archetype archetype;

    Archetypes(Archetype archetype) {
        this.archetype = archetype;
    }

    public static Archetype getRandom(CombatantProfile owner){
        return Archetypes.values()[(new Random()).nextInt(Archetypes.values().length)].getArchetype(owner);
    }

    public Archetype getArchetype(CombatantProfile owner){
        return archetype.getNew(owner);
    }

    public static List<CellTerrains> getAdvantages(Archetypes... archetypes){
        if(archetypes.length == 1){
            return archetypes[0].archetype.getAdvantageous();
        } else {
            List<CellTerrains> give = new CopyOnWriteArrayList<>();
            for(Archetypes a: archetypes){
                give.addAll(a.archetype.getAdvantageous());
            }

            for(Archetypes a: archetypes){
                for(CellTerrains t: give){
                    if(a.archetype.isHindering(t)){
                        give.remove(t);
                    }
                }
            }

            return give;
        }
    }

    public static List<CellTerrains> getHindering(Archetypes... archetypes){
        if(archetypes.length == 1){
            return archetypes[0].archetype.getHindering();
        } else {
            List<CellTerrains> give = new CopyOnWriteArrayList<>();
            for(Archetypes a: archetypes){
                give.addAll(a.archetype.getHindering());
            }

            for(Archetypes a: archetypes){
                for(CellTerrains t: give){
                    if(a.archetype.isAdvantageous(t)){
                        give.remove(t);
                    }
                }
            }

            return give;
        }
    }

}
