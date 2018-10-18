package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.effects.options;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;

public class EffectOptionCenterFollowCaster extends EffectOption<Entity> {

    /**
     * This option will keep the effect following the player. A use for this would be something like a beam,
     * where you want it to constantly come from the character regardless if they move or not
     * @param value
     */
    public EffectOptionCenterFollowCaster(Entity value) {
        super(value, EffectOptionTypes.CENTER_FOLLOW_CASTER);
    }

    /**
     * These should be the offsets found in the iteratePlay()/play() method stored in an array.
     * This will return the adjusted display at with the effect offsets added
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Vector3d getDisplayAt(double x, double y, double z){
        return LocationUtils.getEyeHeight(getValue()).add(x, y, z);
    }
}
