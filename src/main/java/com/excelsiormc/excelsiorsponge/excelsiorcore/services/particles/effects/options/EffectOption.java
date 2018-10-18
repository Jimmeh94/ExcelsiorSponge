package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.effects.options;

public class EffectOption<T> {

    private T value;
    private EffectOptionTypes type;

    public EffectOption(T value, EffectOptionTypes type) {
        this.value = value;
        this.type = type;
    }

    public EffectOptionTypes getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public enum EffectOptionTypes{
        ROTATION_CASTER,
        CENTER_FOLLOW_CASTER,
        STATIC_ROTATION,
        GROW_RADIUS,
        SLEEP,
        CHAIN_EFFECT
    }
}
