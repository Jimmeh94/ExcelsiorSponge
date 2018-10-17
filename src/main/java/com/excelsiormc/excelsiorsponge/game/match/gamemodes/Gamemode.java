package com.excelsiormc.excelsiorsponge.game.match.gamemodes;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.TimeFormatter;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBaseMonster;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel.HotbarHandDummy;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.BattleResult;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.game.user.scoreboard.ArenaDefaultPreset;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
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
import java.util.UUID;

public abstract class Gamemode {

    protected List<Team> teams;
    private int timeLeft;
    private final int totalTime; //in seconds
    private TurnManager turnManager;
    protected String world;
    protected Arena arena;
    protected Stage gameStage = Stage.PRE_GAME;
    private int preGameTimeLimit = 5;
    protected ServerBossBar gameTime, turnTime;

    protected boolean timePaused = false;

    public Gamemode(int timeLimit, int timeLimitForEachTurn, String world){
        teams = new ArrayList<>();

        this.timeLeft = timeLimit;
        this.totalTime = timeLimit;
        turnManager = new TurnManager(timeLimitForEachTurn);
        this.world = world;

        gameTime = ServerBossBar.builder()
                .color(BossBarColors.YELLOW)
                .name(Text.of(TextColors.YELLOW, ""))
                .overlay(BossBarOverlays.PROGRESS)
                .build();

        turnTime = ServerBossBar.builder()
                .color(BossBarColors.RED)
                .name(Text.of(TextColors.RED, ""))
                .overlay(BossBarOverlays.PROGRESS)
                .build();
    }

    protected abstract void tick();
    protected abstract void endingGame();
    protected abstract void startingGame();
    protected abstract void generateSpawnPoints();
    public abstract BattleResult battle(Cell attacker, Cell defender);
    public abstract String getName();

    /**
     * ID of the gamemode.
     * Duel = 0
     * Not sure if this will ever be used or not
     * @return
     */
    public abstract int getID();

    public void start(){
        generateSpawnPoints();
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    PlayerUtils.getUserPlayer(p.getPlayer()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
                    Player player = p.getPlayer();
                    player.setLocation(new Location<World>(player.getWorld(), team.getSpawn()));
                    team.getSpawnCell().setOccupyingCard(p.getCard(), true);

                    UserPlayer user = PlayerUtils.getUserPlayer(p.getUUID()).get();
                    user.changeScoreboardPreset(new ArenaDefaultPreset(user,
                            ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithPlayer(player).get()));
                }
            }
        }
        arena.broadcastMessage(Text.of(TextColors.GOLD, "Countdown will begin in 5 seconds"), Messager.Prefix.DUEL);
        startingGame();
    }

    public void endGame(){
        arena.broadcastMessage(Text.of("GAME OVER"), Messager.Prefix.DUEL);
        arena.getGrid().resetCells();
        endingGame();

        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    Player player = p.getPlayer();
                    gameTime.removePlayer(player);
                    turnTime.removePlayer(player);
                    PlayerUtils.getUserPlayer(player).get().setPlayerMode(UserPlayer.PlayerMode.NORMAL);
                }
            }
        }

        arena.end();
        arena = null;
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
                            userPlayer.setCurrentHotbar(new HotbarHandDummy((CombatantProfilePlayer) c));
                            userPlayer.getCurrentHotbar().setHotbar(userPlayer.getPlayer());
                        }
                    }
                }

                arena.broadcastMessage(Text.of(TextColors.GOLD, "All decks have been shuffled and your hands have been drawn!"),
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

        if(timePaused) {
            return;
        }

        //display boss bar for when the game first starts
        if(timeLeft == totalTime){
            for(Team team: teams){
                for(CombatantProfile c: team.getCombatants()){
                    if(c.isPlayer()){
                        gameTime.addPlayer(c.getPlayer());
                        turnTime.addPlayer(c.getPlayer());
                    }
                }
            }
        }

        if(timeLeft == 0){
            endGame();
        } else {
            if(turnManager.needToStartNextTurn()){
                turnManager.startNextTurn(teams);
            }

            timeLeft--;
            gameTime.setPercent(1.0f * (((float)timeLeft / (float)totalTime)));
            if(gameTime.getPercent() >= 0.66f){
                gameTime.setColor(BossBarColors.GREEN);
            } else if(gameTime.getPercent() >= 0.33f){
                gameTime.setColor(BossBarColors.YELLOW);
            } else {
                gameTime.setColor(BossBarColors.RED);
            }
            gameTime.setName(Text.of(TextColors.YELLOW, "Game Time Left: " + TimeFormatter.getFormattedTime(timeLeft)));
            turnManager.updateBossBar(turnTime);
            turnManager.tick();

            for(Team team: teams){
                team.checkAlive();

                for(CombatantProfile c: team.getCombatants()){
                    if(c.isPlayer()){
                        UserPlayer user = PlayerUtils.getUserPlayer(c.getPlayer()).get();
                        user.updateScoreboard();
                    }
                }
            }
            if(getTotalAliveTeams() == 1){
                endGame();
                return;
            }
        }
        tick();
    }

    private int getTotalAliveTeams(){
        int count = 0;
        for(Team team: teams){
            if(team.isAlive()){
                count++;
            }
        }
        return count;
    }

    public void setTimePaused(boolean timePaused) {
        this.timePaused = timePaused;
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

                    Cell newAim = null;

                    boolean shouldContinue = true;
                    while(ray.hasNext() && shouldContinue){
                        BlockRayHit<World> hit = ray.next();

                        Vector3d pos = hit.getPosition();

                        if (grid.isAimInCell(pos)) {
                            newAim = grid.getCell(pos).get();
                            shouldContinue = false;
                        }
                    }

                    if (cpp.getCurrentAim().isPresent() && cpp.getCurrentAim().get() == newAim) {
                        continue;
                    }

                    cpp.setCurrentAim(newAim);

                    Sponge.getEventManager().post(new DuelEvent.AimUpdated(ExcelsiorSponge.getServerCause(), cpp));
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
        return TimeFormatter.getFormattedTime(timeLeft);
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

    public void handlePlayerEmptyClick(Player player) {
        /**
         * Should bring up info about that cell and the occupying card if there
         */
        CombatantProfilePlayer cpp = DuelUtils.getCombatProfilePlayer(player.getUniqueId()).get();
        if(cpp.getCurrentAim().isPresent() && !cpp.getCurrentAim().get().isAvailable()
                && cpp.getCurrentAim().get().getOccupyingCard() instanceof CardBaseMonster){
            cpp.getCurrentAim().get().getOccupyingCard().displayStats(player);
        }
    }

    public Team getTeamWithCombatant(UUID combatant) {
        for(Team team: teams){
            if(team.isCombatant(combatant)){
                return team;
            }
        }
        return null;
    }

    public boolean isOnSameTeam(UUID one, UUID two) {
        return DuelUtils.getTeam(one).isCombatant(two);
    }

    public void voteToEndTurn(Player player) {
        if(turnManager.voteToEndTurn(player)){
            turnManager.startNextTurn(teams);
        }
    }


    protected class TurnManager {

        private Team currentTurn;
        private final int timeLimitForEachTurn;
        private int timeLeft;

        public TurnManager(int timeLimitForEachTurn) {
            this.timeLimitForEachTurn = timeLimitForEachTurn;
            this.timeLeft = timeLimitForEachTurn;
        }

        public boolean voteToEndTurn(Player player){
            if(currentTurn.isCombatant(player.getUniqueId())){
                return currentTurn.voteToEndTurn(player.getUniqueId());
            }
            return false;
        }

        public void startNextTurn(List<Team> teams){
            if(currentTurn == null){
                currentTurn = teams.get(0);
            } else {
                int index = teams.indexOf(currentTurn);

                for(CombatantProfile c: currentTurn.getCombatants()){
                    if(c.isPlayer()){
                        CombatantProfilePlayer cpp = (CombatantProfilePlayer) c;
                        if(cpp.getUserPlayer().getPlayerMode() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                            cpp.stopMovingCard();
                        } else {
                            cpp.getPlayer().closeInventory();
                            arena.getGrid().redrawGridForPlayer(cpp.getPlayer());
                        }
                    }
                }

                Sponge.getEventManager().post(new DuelEvent.EndTurn(ExcelsiorSponge.getServerCause(), currentTurn));

                currentTurn.clearVotes();
                if(index == teams.size() - 1){
                    currentTurn = teams.get(0);
                } else {
                    currentTurn = teams.get(index + 1);
                }
            }

            Sponge.getEventManager().post(new DuelEvent.BeginTurn(ExcelsiorSponge.getServerCause(), currentTurn));

            timeLeft = timeLimitForEachTurn;
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

        public void tick(){
            timeLeft--;
        }

        public boolean needToStartNextTurn(){
            if(timeLeft == 0 || currentTurn == null){
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
            return TimeFormatter.getFormattedTime(timeLeft);
        }

        public void updateBossBar(ServerBossBar turnTime) {
            turnTime.setName(Text.of(TextColors.RED, "Time Remaining for Turn: " + TimeFormatter.getFormattedTime(timeLeft - 1)));
            turnTime.setPercent(1.0f * (((float)timeLeft / (float)timeLimitForEachTurn)));
            if(turnTime.getPercent() >= 0.66f){
                turnTime.setColor(BossBarColors.GREEN);
            } else if(turnTime.getPercent() >= 0.33f){
                turnTime.setColor(BossBarColors.YELLOW);
            } else {
                turnTime.setColor(BossBarColors.RED);
            }
        }
    }

    public enum Stage {
        PRE_GAME,
        COUNTDOWN,
        IN_GAME,
        POST_GAME
    }


}
