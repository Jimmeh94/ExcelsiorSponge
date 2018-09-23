package com.excelsiormc.excelsiorsponge.excelsiorcore.services.text;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.Stats;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This is an easy way to send paragraphs of texts to players using Messager
 * Child messages will be indented with bullet points by default
 */
public class Message {

    public static Builder builder(){return new Builder();}

    public static Message from(Stats stats, Text statOwner, Player displayTo){
        Builder builder = builder();
        builder.addReceiver(displayTo);
        builder.addAsChild(Text.builder().append(Text.of(TextColors.GRAY, statOwner.toPlain()), Text.of(TextColors.GRAY, "'s Stats")).build(), TextColors.GOLD);

        for(StatBase statBase: ((Collection<StatBase>)stats.getStats())){
            builder.addAsChild(Text.builder().append(statBase.getDisplayName(),
                    Text.of(TextColors.GRAY, ": " + String.valueOf(statBase.getCurrent()) + "/" + String.valueOf(statBase.getMax()))).build(),
                    TextColors.GOLD);
        }
        return builder.build();
    }

    private Entry[] messages;
    private Player[] sendTo;

    private Message(Player[] sendTo, Entry... messages){
        this.messages = messages;
        this.sendTo = sendTo;
    }

    public Entry[] getMessages() {
        return messages;
    }

    public Player[] getSendTo() {
        return sendTo;
    }

    public static class Builder {

        private List<Player> sendTo = new ArrayList<>();
        private List<Entry> messages = new ArrayList<>();

        public Builder addReceiver(Player player){
            sendTo.add(player);
            return this;
        }

        public Builder addMessage(Text text){
            messages.add(new Entry(text, null));
            return this;
        }

        public Builder addMessage(Text text, Messager.Prefix prefix){
            messages.add(new Entry(text, prefix));
            return this;
        }

        public Builder addAsChild(Text text, TextColor bulletColor){
            messages.add(new Entry(text, Messager.Prefix.CHILD, bulletColor));
            return this;
        }

        public Message build(){
            return new Message(sendTo.toArray(new Player[sendTo.size()]), messages.toArray(new Entry[messages.size()]));
        }

        public Builder append(Message message){
            for(Player player: message.sendTo){
                if(!this.sendTo.contains(player)){
                    this.sendTo.add(player);
                }
            }

            this.messages.addAll(Arrays.asList(message.messages));
            return this;
        }

    }

    public static class Entry{
        private Text text;
        private Messager.Prefix prefix;
        private TextColor bulletColorForChild;

        public Entry(Text text, Messager.Prefix prefix) {
            this.text = text;
            this.prefix = prefix;
        }

        public Entry(Text text, Messager.Prefix prefix, TextColor bulletColorForChild) {
            this.text = text;
            this.prefix = prefix;
            this.bulletColorForChild = bulletColorForChild;
        }

        public TextColor getBulletColorForChild() {
            return bulletColorForChild;
        }

        public Text getText() {
            return text;
        }

        public Messager.Prefix getPrefix() {
            return prefix;
        }
    }
}
