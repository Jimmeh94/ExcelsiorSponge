package com.excelsiormc.excelsiorsponge.game.chatchannels;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat.channel.ChatChannel;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ChatChannelStaff extends ChatChannel {

    public ChatChannelStaff() {
        super(ChatChannelKeys.StaffChannel, true, Text.of(TextColors.DARK_RED, "[Staff] "));
    }
}
