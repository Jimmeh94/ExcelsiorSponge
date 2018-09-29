package com.excelsiormc.excelsiorsponge;

import com.excelsiormc.excelsiorsponge.commands.ArenaCommands;
import com.excelsiormc.excelsiorsponge.events.DuelEvents;
import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.event.ChatEvents;
import com.excelsiormc.excelsiorsponge.events.PlayerEvents;
import com.excelsiormc.excelsiorsponge.events.WorldEvents;
import com.excelsiormc.excelsiorsponge.game.chatchannels.ChatChannelAuction;
import com.excelsiormc.excelsiorsponge.game.chatchannels.ChatChannelStaff;
import com.excelsiormc.excelsiorsponge.game.economy.currencies.Currencies;
import com.excelsiormc.excelsiorsponge.game.match.matchmaking.MatchMaker;
import com.excelsiormc.excelsiorsponge.timers.ArenaTimer;
import com.excelsiormc.excelsiorsponge.timers.DirectionalAimArenaTimer;
import com.excelsiormc.excelsiorsponge.utils.database.MongoUtils;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.World;

@Plugin(
        id = "excelsiorsponge",
        name = "ExcelsiorSponge",
        description = "A Sponge based card game insired by YuGiOh and HearthStone"
)
public class ExcelsiorSponge {

    //TODO mongo utils need to write cell terrain build start locations and type

    @Inject
    private Logger logger;

    public static ExcelsiorSponge INSTANCE;
    public static PluginContainer PLUGIN_CONTAINER;

    private MatchMaker matchMaker;
    private MongoUtils mongoUtils;

    private ArenaTimer arenaTimer;
    private DirectionalAimArenaTimer directionalAimArenaTimer;

    @Listener
    public void onGameInit(GameInitializationEvent event){
        new ExcelsiorCore();
        INSTANCE = this;
        PLUGIN_CONTAINER = Sponge.getPluginManager().fromInstance(this).get();

        mongoUtils = new MongoUtils("Admin", "admin1234", "@ds149742.mlab.com:49742/excelsior", "excelsior");
        mongoUtils.openConnection();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        matchMaker = new MatchMaker();

        registerListeners();
        registerRunnables();
        registerCommands();

        ExcelsiorCore.INSTANCE.getEconomy().addCurrency(Currencies.GOLD);
        ExcelsiorCore.INSTANCE.getEconomy().addDefault(Currencies.GOLD, 500);

        ExcelsiorCore.INSTANCE.getChannelManager().add(new ChatChannelAuction());
        ExcelsiorCore.INSTANCE.getChannelManager().add(new ChatChannelStaff());

        mongoUtils.load();

        clearAllEntities();
    }

    public void clearAllEntities(){
        for(World w: Sponge.getServer().getWorlds()){
            for(Entity e: w.getEntities()){
                if(e instanceof ArmorStand || e instanceof Human){
                    e.remove();
                }
            }
        }
    }

    private void registerCommands() {
        new ArenaCommands();
    }

    private void registerRunnables() {
        arenaTimer = new ArenaTimer(20L);
        arenaTimer.start();

        directionalAimArenaTimer = new DirectionalAimArenaTimer(3L);
        directionalAimArenaTimer.start();
    }

    private void registerListeners() {
        //Core events
        Sponge.getEventManager().registerListeners(this, new ChatEvents());

        //Excelsior events
        Sponge.getEventManager().registerListeners(this, new DuelEvents());
        Sponge.getEventManager().registerListeners(this, new PlayerEvents());
        Sponge.getEventManager().registerListeners(this, new WorldEvents());
    }

    @Listener
    public void onServerStopping(GameStoppingEvent event){
        //getMatchMaker().getArenaManager().clean();
        mongoUtils.close();
    }

    public MatchMaker getMatchMaker() {
        return matchMaker;
    }

    public static Cause getServerCause(){
        EventContext context = EventContext.builder().add(EventContextKeys.PLUGIN, PLUGIN_CONTAINER).build();
        return Cause.builder().append(PLUGIN_CONTAINER).build(context);
    }

    public ArenaTimer getArenaTimer() {
        return arenaTimer;
    }

    public DirectionalAimArenaTimer getDirectionalAimArenaTimer() {
        return directionalAimArenaTimer;
    }
}
