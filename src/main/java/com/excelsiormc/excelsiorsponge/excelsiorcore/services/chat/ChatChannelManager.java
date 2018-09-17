package com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Manager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel.ChatChannel;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel.ChatChannelGlobal;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatChannelManager extends Manager<ChatChannel> {

    private List<ChatPlayerProfile> profiles;

    public static final ChatChannel GLOBAL = new ChatChannelGlobal("");

    public ChatChannelManager() {
        profiles = new CopyOnWriteArrayList<>();
        add(GLOBAL);
    }


    public boolean isKeyAvailable(String key){
        return !findChannel(key).isPresent();
    }

    private Optional<ChatChannel> findChannel(String key){
        for(ChatChannel chatChannel: objects){
            if(chatChannel.getKey().equalsIgnoreCase(key)){
                return Optional.of(chatChannel);
            }
        }
        return Optional.empty();
    }

    public void setToDefault(ChatPlayerProfile ChatPlayerProfile){
        ChatPlayerProfile.setChatChannel(GLOBAL);
    }

    public void setChannel(PlayerBase base, String key){
        Optional<ChatChannel> channel = findChannel(key);

        if(channel.isPresent() && channel.get() != base.getChatProfile().getChatChannel()){
            base.getChatProfile().setChatChannel(channel.get());
            Messager.sendMessage(Message.builder().addReceiver(base.getChatProfile().getPlayer()).addMessage(Text.of(TextColors.GRAY, "Moved to chat channel: " + channel.get().getKey()), Messager.Prefix.SUCCESS).build());
        } else {
            Messager.sendMessage(Message.builder().addReceiver(base.getChatProfile().getPlayer()).addMessage(Text.of(TextColors.GRAY, "The channel doesn't exist or you are already in it: " + key), Messager.Prefix.ERROR).build());
        }
    }

    public void removePlayerFromAllChannels(PlayerBase base){
        if(GLOBAL.hasMember(base.getChatProfile())){
            GLOBAL.removeMember(base.getChatProfile());
        }
        for(ChatChannel chatChannel: objects){
            if(chatChannel.hasMember(base.getChatProfile())){
                chatChannel.removeMember(base.getChatProfile());
            }
        }
    }
    
}
