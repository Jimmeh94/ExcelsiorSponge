package com.excelsiormc.excelsiorsponge.excelsiorcore;

import com.excelsiormc.excelsiorsponge.excelsiorcore.event.ChatEvents;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatChannelManager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy.Economy;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.party.PartyManager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBaseManager;
import org.spongepowered.api.Sponge;

/*
@Plugin(
        id = "excelsiorcore",
        name = "ExcelsiorCore",
        description = "A Sponge based API with basic utilities.",
        authors = {
                "Jimmy"
        }
)*/
public class ExcelsiorCore {

    public static ExcelsiorCore INSTANCE;

    private Economy economy;
    private PartyManager partyManager;
    private ChatChannelManager channelManager;
    private PlayerBaseManager playerBaseManager;

    public ExcelsiorCore() {
        INSTANCE = this;

        economy = new Economy();
        partyManager = new PartyManager();
        channelManager = new ChatChannelManager();
        playerBaseManager = new PlayerBaseManager();

        //registerListeners();
        //registerRunnables();
        //registerCommands();
    }

    private void registerCommands() {

    }

    private void registerRunnables() {

    }

    private void registerListeners() {
        Sponge.getEventManager().registerListeners(this, new ChatEvents());

    }

    public PlayerBaseManager getPlayerBaseManager() {
        return playerBaseManager;
    }

    public ChatChannelManager getChannelManager() {
        return channelManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }
}
