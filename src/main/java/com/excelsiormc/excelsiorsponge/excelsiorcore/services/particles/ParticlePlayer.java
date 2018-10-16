package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOption;

import java.util.Iterator;
import java.util.Map;

public class ParticlePlayer {

    public static void display(ParticleData data){
        ParticleEffect.Builder builder = ParticleEffect.builder();

        builder.type(data.getType());
        builder.offset(data.getOffsetVector());

        if(data.getVelocity().isPresent()){
            builder.velocity(data.getVelocity().get());
        }

        if(data.getParticleOptions().isPresent()){
            Iterator<Map.Entry<ParticleOption, Object>> iterator = data.getParticleOptions().get().entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<ParticleOption, Object> pair = iterator.next();
                builder.option(pair.getKey(), pair.getValue());
            }
        }

        for(PlayerBase user: data.getViewers()){
            user.getPlayer().spawnParticles(builder.quantity((int) (data.getQuantity() * user.getParticleModifier().getScale())).build(),
                    data.getDisplayAt());
        }
    }

}
