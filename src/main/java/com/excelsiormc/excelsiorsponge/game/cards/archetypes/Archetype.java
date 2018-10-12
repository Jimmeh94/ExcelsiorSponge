package com.excelsiormc.excelsiorsponge.game.cards.archetypes;

import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Archetype {

    protected List<CellTerrains> advantageous, hindering;
    protected Optional<CombatantProfile> owner;

    public Archetype() {
        advantageous = new CopyOnWriteArrayList<>();
        hindering = new CopyOnWriteArrayList<>();
        owner = Optional.empty();

        setAdvantageousTerrain();
        setHinderingTerrain();
    }

    protected Archetype(Archetype a, CombatantProfile owner) {
        this.advantageous = new CopyOnWriteArrayList<>(a.advantageous);
        this.hindering = new CopyOnWriteArrayList<>(a.hindering);
        this.owner = Optional.of(owner);
    }

    protected abstract void setAdvantageousTerrain();
    protected abstract void setHinderingTerrain();
    protected abstract Archetype getNew(CombatantProfile owner);

    protected void addAdvantageousTerrain(CellTerrains... terrains){
        for(CellTerrains c: terrains){
            if(!advantageous.contains(c)){
                advantageous.add(c);
            }
        }
    }

    protected void addHinderingTerrain(CellTerrains... terrains){
        for(CellTerrains c: terrains){
            if(!hindering.contains(c)){
                hindering.add(c);
            }
        }
    }

    public List<CellTerrains> getAdvantageous() {
        return advantageous;
    }

    public List<CellTerrains> getHindering() {
        return hindering;
    }

    public Optional<CombatantProfile> getOwner() {
        return owner;
    }

    public boolean isAdvantageous(CellTerrains cellTerrain){
        return advantageous.contains(cellTerrain);
    }

    public boolean isNeutral(CellTerrains cellTerrain){
        return !isAdvantageous(cellTerrain) && !isHindering(cellTerrain);
    }

    public boolean isHindering(CellTerrains cellTerrain){
        return hindering.contains(cellTerrain);
    }
}
