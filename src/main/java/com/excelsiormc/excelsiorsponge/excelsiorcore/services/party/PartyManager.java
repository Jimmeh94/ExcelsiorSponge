package com.excelsiormc.excelsiorsponge.excelsiorcore.services.party;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Manager;

import java.util.Optional;
import java.util.UUID;

public class PartyManager extends Manager<Party> {

    public Optional<Party> findParty(UUID uuid){
        for(Party p: objects){
            if(p.isMember(uuid)){
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

}
