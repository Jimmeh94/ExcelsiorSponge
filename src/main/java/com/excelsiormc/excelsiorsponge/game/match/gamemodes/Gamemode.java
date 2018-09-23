package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.TimeFormatter;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel.HotbarHand;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Gamemode {

    protected List<Team> teams;
    private int timeLimit; //in seconds
    private TurnManager turnManager;
    protected String world;
    protected Arena arena;
    protected Stage gameStage = Stage.PRE_GAME;
    private int preGameTimeLimit = 5;

    public Gamemode(int timeLimit, int timeLimitForEachTurn, String world){
        teams = new ArrayList<>();

        this.timeLimit = timeLimit;
        turnManager = new TurnManager(timeLimitForEachTurn);
        this.world = world;
    }

    protected abstract void tick();
    protected abstract void endingGame();
    protected abstract void startingGame();
    public abstract String getName();

    /**
     * ID of the gamemode.
     * Duel = 0
     * Not sure if this will ever be used or not
     * @return
     */
    public abstract int getID();

    public void start(Location start){
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    PlayerUtils.getUserPlayer(p.getPlayer()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
                    p.getPlayer().setLocation(start);
                }
            }
        }
        arena.broadcastMessage(Text.of(TextColors.GOLD, "Countdown will begin in 5 seconds"), Messager.Prefix.DUEL);
        startingGame();
    }

    public void endGame(){
        arena.getGrid().resetCells();
        endingGame();
        arena = null;
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    PlayerUtils.getUserPlayer(p.getPlayer()).get().setPlayerMode(UserPlayer.PlayerMode.NORMAL);
                    //Teleport out
                    //Bukkit.getPlayer(p.getUUID()).teleport(new Location(Bukkit.getWorld(world), start.getX(), start.getY(), start.getZ()));
                }
            }
        }
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void baseTick(){

        if(gameStage == Stage.PRE_GAME){
            //give 5 seconds for orientation
            //Shuffle decks
            //shift stage to countdown
            preGameTimeLimit--;
            if(preGameTimeLimit == 1){
                for(Team team: teams){
                    for(CombatantProfile c: team.getCombatants()){
                        c.getDeck().shuffleCards();
                        c.drawHand();
                        if(c.isPlayer()){
                            UserPlayer userPlayer = PlayerUtils.getUserPlayer(c.getPlayer()).get();
                            userPlayer.setCurrentHotbar(new HotbarHand((CombatantProfilePlayer) c));
                            userPlayer.getCurrentHotbar().setHotbar(userPlayer.getPlayer());
                        }
                    }
                }

                arena.broadcastMessage(Text.of(TextColors.GOLD + "All decks have been shuffled and your hands have been drawn!"),
                        Messager.Prefix.DUEL);

            } else if(preGameTimeLimit == 0){
                gameStage = Stage.COUNTDOWN;
                preGameTimeLimit = 10;
            }

            if(preGameTimeLimit > 0) {
                return;
            }

        } else if(gameStage == Stage.COUNTDOWN){
            TextColor color;
            if(preGameTimeLimit > 6){
                color = TextColors.GREEN;
            } else if(preGameTimeLimit <= 6 && preGameTimeLimit > 3){
                color = TextColors.YELLOW;
            } else {
                color = TextColors.RED;
            }
            arena.broadcastMessage(Text.of(color, "The " + getName() + " begins in " + preGameTimeLimit + "s"), Messager.Prefix.DUEL);
            preGameTimeLimit--;

            if(preGameTimeLimit == 0){
                gameStage = Stage.IN_GAME;
                for(Team team: teams){
                    for(CombatantProfile c: team.getCombatants()){
                        Hotbars.HOTBAR_WAITING_TURN.setHotbar(c.getPlayer());
                    }
                }
            }

            return;
        }

        timeLimit--;
        if(timeLimit == 0){
            endGame();
        } else {
            for(Team team: teams){
                if(team.isEmptyOfPlayers()){
                    //TODO if not a PlayerVsAI game or if players don't want them to be repalced by bots
                    //TODO game should end and the other team should win
                } else {
                    for(CombatantProfile c: team.getCombatants()){
                        if(c.isPlayer()){
                            UserPlayer user = PlayerUtils.getUserPlayer(c.getPlayer()).get();
                            user.updateScoreboard();
                        }
                    }
                }
            }

            if(turnManager.needToStartNextTurn()){
                turnManager.startNextTurn(teams);
            }
        }
        tick();
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public void updatePlayersAim(Grid grid) {
        if(gameStage != Stage.IN_GAME){
            return;
        }

        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    UserPlayer.PlayerMode mode = PlayerUtils.getUserPlayer(p.getUUID()).get().getPlayerMode();
                    CombatantProfilePlayer cpp = (CombatantProfilePlayer) p;
                    Player player = p.getPlayer();

                    //Update their aim

                    BlockRay<World> ray = BlockRay.from(player).distanceLimit(100)
                            .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();

                    Optional<BlockRayHit<World>> hitOpt = ray.end();
                    Cell newAim = null;
                    if(hitOpt.isPresent()){
                        Vector3d pos = hitOpt.get().getPosition();

                        if (grid.isCell(pos)) {
                            newAim = grid.getCell(pos).get();
                        }
                    }

                    if(mode == UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT) {
                        //Make sure targeting retcile needs to appear
                        if (cpp.getCurrentAim() != null && cpp.getCurrentAim() == newAim) {
                            continue;
                        }
                        if (cpp.getCurrentAim() != null) {
                            cpp.getCurrentAim().clearAimForPlayer(player);
                        }
                        cpp.setCurrentAim(newAim);

                        if(cpp.getCurrentAim() != null) {
                            cpp.getCurrentAim().drawAimForPlayer(player);
                        }

                    } else if(mode == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                        //We only want to highlight a cell that the card can move to

                        cpp.setCurrentAim(newAim);

                    } else if(mode == UserPlayer.PlayerMode.ARENA_VIEWING_CARD_INFO){

                        cpp.setCurrentAim(newAim);

                    }
                }
            }
        }
    }

    public boolean isPlayerCombatant(Player player) {
        for(Team team: teams){
            if(team.isPlayerCombatant(player)){
                return true;
            }
        }
        return false;
    }

    public void playerQuit(Player player) {
        for(Team team: teams){
            if(team.isPlayerCombatant(player)){
                team.playerQuit(player);
            }
        }
        //TODO do something here. Give option to other players to continue, continue with bot, or leave
    }

    public String getTimeLeftFormatted() {
        return TimeFormatter.getFormattedTime(timeLimit);
    }

    public boolean isPlayersTurn(Player owner) {
        return turnManager.isPlayersTurn(owner);
    }

    public String getTimeLeftInCurrentTurnFormatted() {
        return turnManager.getTimeLeftInCurrentTurn();
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Stage getStage() {
        return gameStage;
    }

    public void handlePlayerRightEmptyClick() {
        /**
         * Should bring up info about that cell and the occupying card if there
         */
    }


    protected class TurnManager {

        private Team currentTurn;
        private int timeLimitForEachTurn;
        private int elapsedTime;

        public TurnManager(int timeLimitForEachTurn) {
            this.timeLimitForEachTurn = timeLimitForEachTurn;
        }

        public void startNextTurn(List<Team> teams){
            if(currentTurn == null){
                currentTurn = teams.get(0);
            } else {
                int index = teams.indexOf(currentTurn);
                if(index == teams.size() - 1){
                    currentTurn = teams.get(0);
                } else {
                    currentTurn = teams.get(index + 1);
                }
            }
            elapsedTime = 0;
            currentTurn.drawCard();
            currentTurn.broadcastStartTurnMessage();

            for(Team team: teams){
                if(team == currentTurn){
                    for(CombatantProfile c: team.getCombatants()){
                        if(c.isPlayer()){
                            Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(c.getPlayer());
                        }
                    }
                } else {
                    for(CombatantProfile c: team.getCombatants()){
                        if(c.isPlayer()){
                            Hotbars.HOTBAR_WAITING_TURN.setHotbar(c.getPlayer());
                        }
                    }
                    team.broadcastEndTurnMessage(Text.of(TextColors.GRAY, "Other team's turn now"));
                }
            }
        }

        public boolean needToStartNextTurn(){
            elapsedTime++;
            if(elapsedTime >= timeLimitForEachTurn || currentTurn == null){
                return true;
            }
            return false;
        }

        public boolean isPlayersTurn(Player owner) {
            for(Team team: teams){
                if(team.isPlayerCombatant(owner) && team == currentTurn){
                    return true;
                }
            }
            return false;
        }

        public String getTimeLeftInCurrentTurn() {
            return TimeFormatter.getFormattedTime(timeLimitForEachTurn - elapsedTime);
        }
    }

    public enum Stage {
        PRE_GAME,
        COUNTDOWN,
        IN_GAME,
        POST_GAME
    }


}
