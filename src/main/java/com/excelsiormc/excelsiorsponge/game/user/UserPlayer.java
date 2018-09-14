package com.excelsiormc.excelsiorsponge.game.user;

import com.excelsiormc.excelsiorcore.services.scoreboard.Scoreboard;
import com.excelsiormc.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.entity.living.player.Player;

public class UserPlayer extends PlayerBase {
    private Scoreboard scoreboard;
    private Deck deck;
    private PlayerMode playerMode;
    private Hotbar currentHotbar;

    public UserPlayer(Player player){
        super(player.getUniqueId(), ServiceParticles.ParticleModifier.NORMAL);

        playerMode = PlayerMode.NORMAL;
        deck = new DeckDummy(getUUID());

        scoreboard = new Scoreboard(player, new DefaultPreset(player));
    }

    public void setCurrentHotbar(Hotbar currentHotbar) {
        this.currentHotbar = currentHotbar;
    }

    public Hotbar getCurrentHotbar() {
        return currentHotbar;
    }

    public void setPlayerMode(PlayerMode playerMode) {
        Bukkit.getPluginManager().callEvent(new PlayerModeChangeEvent(CustomEvent.SERVER_CAUSE, this.playerMode, playerMode, getPlayer()));
        this.playerMode = playerMode;
    }

    public PlayerMode getPlayerMode() {
        return playerMode;
    }

    public Deck getDeck() {
        return deck;
    }

    public void changeScoreboardPreset(ScoreboardPreset preset){
        scoreboard.setPreset(preset);
    }

    public void updateScoreboard() {
        scoreboard.updateScoreboard();
    }

    /**
     * This just makes it easier for dividing up the interact events
     */
    public enum PlayerMode{
        ARENA_ADD,
        ARENA_DUEL_DEFAULT,
        ARENA_VIEWING_CARD_INFO,
        ARENA_MOVING_CARD,

        NORMAL;
    }
}
