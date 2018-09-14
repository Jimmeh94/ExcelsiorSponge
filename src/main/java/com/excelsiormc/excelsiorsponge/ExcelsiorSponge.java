package com.excelsiormc.excelsiorsponge;

import com.excelsiormc.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorcore.services.database.ServiceMongoDB;
import com.excelsiormc.excelsiorsponge.managers.ManagerArena;
import com.excelsiormc.excelsiorsponge.utils.database.MongoUtils;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
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
        arenaManager = new ManagerArena();
    }

    @Listener
    public void onServerStopping(GameStoppingEvent event){
        mongoUtils.close();
    }

    public ManagerArena getArenaManager() {
        return arenaManager;
    }
}
