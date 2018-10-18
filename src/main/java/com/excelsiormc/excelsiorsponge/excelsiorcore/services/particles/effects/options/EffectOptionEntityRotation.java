package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.effects.options;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.flowpowered.math.TrigMath;
import org.spongepowered.api.entity.Entity;

/**
 * Updates each tick of an effect/animation and adjusts to the entity's rotation
 */
public class EffectOptionEntityRotation extends EffectOption<Entity> implements EffectOptionRotation{

    public EffectOptionEntityRotation(Entity value) {
        super(value, EffectOptionTypes.ROTATION_CASTER);
    }

    public Pair<Double, Double> getYAxis(){
        double yaw = getValue().getRotation().getY();
        double yangle = Math.toRadians(yaw); // note that here we do have to convert to radians.

        return new Pair<>((double)TrigMath.cos(-yangle), (double)TrigMath.sin(-yangle));
    }

    public Pair<Double, Double> getZAxis(){
        double pitch = getValue().getRotation().getX();
        double zangle = Math.toRadians(pitch); // note that here we do have to convert to radians.

        return new Pair<>((double)TrigMath.cos(zangle), (double)TrigMath.sin(zangle));
    }
}
