package com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.PlayerBase;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class ChatChannelGlobal extends ChatChannel {

    private static final Text prefix = Text.of(TextColors.GRAY, "(Global) ");

    public ChatChannelGlobal(String key) {
        super(key, true, prefix);

        passwordProtected = false;
    }

    public ChatChannelGlobal(String key, PlayerBase owner) {
        super(key, true, prefix, owner);
    }

    public ChatChannelGlobal(String key, PlayerBase... members) {
        super(key, true, prefix, members);
    }

    public ChatChannelGlobal(String key, List<PlayerBase> members) {
        super(key, true, prefix, members);
    }

}
