package com.excelsiormc.excelsiorsponge.excelsiorcore.services.user;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Manager;

import java.util.Optional;
import java.util.UUID;

public class PlayerBaseManager extends Manager<PlayerBase> {

    public Optional<PlayerBase> getPlayerBase(UUID uuid){
        for(PlayerBase base: objects){
            if(base.isOwner(uuid)){
                return Optional.of(base);
            }
        }
        return Optional.empty();
    }

}
