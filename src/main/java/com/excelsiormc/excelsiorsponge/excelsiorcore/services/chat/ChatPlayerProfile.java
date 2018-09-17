package com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel.ChatChannel;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel.ChatChannelReceiver;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class ChatPlayerProfile {

    private ChatColorTemplate colors;
    private ChatPlayerTitle title;
    private UUID owner;
    private ChatChannel currentChatChannel;
    private ChatChannelReceiver receiverChannel;

    public ChatPlayerProfile(ChatColorTemplate colors, ChatPlayerTitle title, UUID owner) {
        this.colors = colors;
        this.title = title;
        this.owner = owner;
        receiverChannel = new ChatChannelReceiver(this);
    }

    public ChatChannelReceiver getReceiverChannel() {
        return receiverChannel;
    }

    public ChatChannel getCurrentChatChannel() {
        return currentChatChannel;
    }

    public ChatColorTemplate getColors() {
        return colors;
    }

    public ChatPlayerTitle getTitle() {
        return title;
    }

    public UUID getOwner() {
        return owner;
    }

    public Player getPlayer(){
        return Sponge.getServer().getPlayer(owner).get();
    }

    public void setChatChannel(ChatChannel channel) {
        this.currentChatChannel = channel;
    }

    public ChatChannel getChatChannel() {
        return currentChatChannel;
    }
}
