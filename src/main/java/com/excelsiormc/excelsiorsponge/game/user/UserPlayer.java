package com.excelsiormc.excelsiorsponge.game.user;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatColorTemplate;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatPlayerProfile;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatPlayerTitle;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.Scoreboard;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.scoreboard.presets.ScoreboardPreset;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.PlayerEvents;
import com.excelsiormc.excelsiorsponge.game.cards.Deck;
import com.excelsiormc.excelsiorsponge.game.cards.decks.DeckDummy;
import com.excelsiormc.excelsiorsponge.game.user.scoreboard.DefaultPreset;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class UserPlayer extends PlayerBase {

    private Scoreboard scoreboard;
    private Deck deck;
    private PlayerMode playerMode;
    private Hotbar currentHotbar;

    public UserPlayer(Player player){
        super(player.getUniqueId(), new ChatPlayerProfile(ChatColorTemplate.GRAY, ChatPlayerTitle.TEST, player.getUniqueId()));

        getChatProfile().setToDefault();

        playerMode = PlayerMode.NORMAL;
        deck = new DeckDummy(getOwner());

        scoreboard = new Scoreboard(this, new DefaultPreset(this));
    }

    public void setCurrentHotbar(Hotbar currentHotbar) {
        this.currentHotbar = currentHotbar;
    }

    public Hotbar getCurrentHotbar() {
        return currentHotbar;
    }

    public void setPlayerMode(PlayerMode playerMode) {
        Sponge.getEventManager().post(new PlayerEvents.PlayerModeChangeEvent(ExcelsiorSponge.getServerCause(), this.playerMode, playerMode, getPlayer()));
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

    public boolean isInDuel() {
        return playerMode == PlayerMode.ARENA_DUEL_DEFAULT || playerMode == PlayerMode.ARENA_MOVING_CARD;
    }

    /**
     * This just makes it easier for dividing up the interact events
     */
    public enum PlayerMode{
        ARENA_DUEL_DEFAULT,
        ARENA_MOVING_CARD,

        NORMAL;
    }
}
