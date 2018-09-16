package com.excelsiormc.excelsiorsponge.game.chatchannels;

import com.excelsiormc.excelsiorcore.services.chat.channel.ChatChannel;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ChatChannelAuction extends ChatChannel {

    public ChatChannelAuction() {
        super(ChatChannelKeys.AuctionChannel, true, Text.of(TextColors.GOLD, "[Auction] "));
    }
}
