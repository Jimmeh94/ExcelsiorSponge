package com.excelsiormc.excelsiorsponge;

import com.excelsiormc.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorcore.services.database.ServiceMongoDB;
import com.excelsiormc.excelsiorsponge.events.PlayerEvents;
import com.excelsiormc.excelsiorsponge.events.WorldEvents;
import com.excelsiormc.excelsiorsponge.game.chatchannels.ChatChannelAuction;
import com.excelsiormc.excelsiorsponge.game.chatchannels.ChatChannelStaff;
import com.excelsiormc.excelsiorsponge.game.economy.currencies.Currencies;
import com.excelsiormc.excelsiorsponge.game.economy.currencies.CurrencyGold;
import com.excelsiormc.excelsiorsponge.managers.ManagerArena;
import com.excelsiormc.excelsiorsponge.timers.ArenaTimer;
import com.excelsiormc.excelsiorsponge.timers.DirectionalAimArenaTimer;
import com.excelsiormc.excelsiorsponge.utils.database.MongoUtils;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = "excelsiorsponge",
        name = "ExcelsiorSponge",
        description = "A Sponge based card game insired by YuGiOh and HearthStone"
)
public class ExcelsiorSponge {

    @Inject
    private Logger logger;

    public static ExcelsiorSponge INSTANCE;
    public static PluginContainer PLUGIN_CONTAINER;

    private ManagerArena arenaManager;
    private MongoUtils mongoUtils;

    @Listener
    public void onGameInit(GameInitializationEvent event){
        INSTANCE = this;
        PLUGIN_CONTAINER = Sponge.getPluginManager().fromInstance(this).get();

        mongoUtils = new MongoUtils("Admin", "admin", "@ds149742.mlab.com:49742/excelsior", "excelsior");
        mongoUtils.openConnection();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        new ExcelsiorCore();

        arenaManager = new ManagerArena();

        registerListeners();
        registerRunnables();
        registerCommands();

        ExcelsiorCore.INSTANCE.getEconomy().addCurrency(Currencies.GOLD);
        ExcelsiorCore.INSTANCE.getEconomy().addDefault(Currencies.GOLD, 500);

        ExcelsiorCore.INSTANCE.getChannelManager().add(new ChatChannelAuction());
        ExcelsiorCore.INSTANCE.getChannelManager().add(new ChatChannelStaff());
    }

    private void registerCommands() {

    }

    private void registerRunnables() {
        (new ArenaTimer(20L)).start();
        (new DirectionalAimArenaTimer(20L)).start();
    }

    private void registerListeners() {
        Sponge.getEventManager().registerListeners(this, new PlayerEvents());
        Sponge.getEventManager().registerListeners(this, new WorldEvents());
    }

    @Listener
    public void onServerStopping(GameStoppingEvent event){
        mongoUtils.close();
    }

    public ManagerArena getArenaManager() {
        return arenaManager;
    }

    public static Cause getEmptyCause(){return Cause.builder().build(EventContext.empty());}
}
