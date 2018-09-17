package com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.ExcelsiorCore;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.ChatPlayerProfile;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ChatChannel{

    private String key;
    private Text prefix;
    protected List<PlayerBase> members = new CopyOnWriteArrayList<>();
    private boolean permanent = false;
    protected boolean passwordProtected = true;

    public ChatChannel(String key, boolean p, Text prefix){
        this.key = key;
        this.permanent = p;
        this.prefix = prefix;
    }

    public ChatChannel(String key, boolean p, Text prefix, PlayerBase owner) {
        this(key, p, prefix, new PlayerBase[]{owner});
    }

    public ChatChannel(String key, boolean p, Text prefix, PlayerBase... members) {
        this(key, p, prefix, Arrays.asList(members));
    }
    
    public ChatChannel(String key, boolean p, Text prefix, List<PlayerBase> members){
        this.members.addAll(members);
        this.key = key;
        this.permanent = p;
        this.prefix = prefix.trim();

        ExcelsiorCore.INSTANCE.getChannelManager().add(this);

        for(PlayerBase base: members){
            ExcelsiorCore.INSTANCE.getChannelManager().setChannel(base, this.key);
        }
    }

    public void displayMessage(Text message){
        for(PlayerBase base: members){
            base.getPlayer().sendMessage(Text.builder().append(prefix).append(Text.of(prefix.getColor(), "| ")).append(message).build());
        }
    }

    public String getKey() {
        return key;
    }

    public boolean hasMember(ChatPlayerProfile profile){return members.contains(profile);}

    public List<PlayerBase> getMembers() {
        return members;
    }

    public boolean addMember(PlayerBase member) {
        if(members.contains(member))
            return false;
        return this.members.add(member);
    }

    public void removeMember(ChatPlayerProfile member) {
        this.members.remove(member);
        if(members.size() == 0 && !permanent){
            ExcelsiorCore.INSTANCE.getChannelManager().remove(this);
        }
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public Text getPrefix() {
        return prefix;
    }
}
