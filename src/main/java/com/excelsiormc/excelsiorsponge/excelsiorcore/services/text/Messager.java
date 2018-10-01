package com.excelsiormc.excelsiorsponge.excelsiorcore.services.text;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy.Account;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy.Currency;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;

import java.util.Optional;

public class Messager {

    public static void sendMessage(Player player, Text text){
        player.sendMessage(text);
    }

    public static void sendBlankLine(Player player){
        player.sendMessage(Text.of(" "));
    }

    public static void sendMessage(Player player, Text text, Prefix prefix){
        player.sendMessage(Text.builder().append(prefix.getText()).append(text).build());
    }

    public static void sendEconomyCantAffordMessage(Account account, Currency currency, double needed){
        sendMessage(Message.builder().addReceiver(PlayerUtils.getPlayer(account.getOwner()).get())
                .addMessage(Text.of(TextColors.RED, "You cannot afford this! Needed: " + currency.getSymbol().toPlain() + needed
                                + TextColors.GREEN + " Have: " + currency.getSymbol().toPlain() + account.getBalance(currency)), Prefix.ECO)
                .build());
    }

    public static void clearChat(Player player){
        for(int i = 0; i < 25; i++){
            sendBlankLine(player);
        }
    }

    public static void sendMessage(Message message){
        for(Player player: message.getSendTo()){
            for(Message.Entry entry: message.getMessages()){
                if(entry.getPrefix() != null){
                    if(entry.getPrefix() == Prefix.CHILD){
                        sendMessage(player, Text.builder().append(Text.of(entry.getBulletColorForChild(), "   " + AltCodes.BULLET_POINT.getSign() + " ")).append(entry.getText()).build());
                    } else {
                        sendMessage(player, Text.builder().append(entry.getPrefix().getText()).append(entry.getText()).build());
                    }
                } else {
                    sendMessage(player, entry.getText());
                }
            }
        }
    }

    public static void sendActionBarMessage(Player player, Text text){
        player.sendMessage(ChatTypes.ACTION_BAR, text);
    }

    public static void sendTitleMessage(Player player, Text text){player.sendTitle(Title.of(text));}

    public static void sendTitleAndSubTitle(Player player, Text title, Text subtitle){player.sendTitle(Title.of(title, subtitle));}

    public static void broadcastMessage(Text text, Optional<Prefix> prefix){
        if(prefix.isPresent()){
            text = Text.builder().append(prefix.get().getText()).append(text).build();
        }
        for(Player p: Sponge.getServer().getOnlinePlayers()){
            p.sendMessage(text);
        }

    }

    public static void sendEconomyPayMessage(Player payer, Player receiver, Currency currency, double amount) {
        sendMessage(payer, Text.of(TextColors.GREEN, "You paid " + receiver.getDisplayNameData().displayName().get().toPlain() +
                " " + currency.getSymbol().toPlain() + amount), Prefix.ECO);
        sendMessage(receiver, Text.of(TextColors.GREEN, payer.getDisplayNameData().displayName().get().toPlain() + " paid you " +
                currency.getSymbol().toPlain() + amount), Prefix.ECO);
    }

    public enum Prefix{
        ERROR(Text.of(TextColors.RED, TextStyles.BOLD, "[" + AltCodes.THICK_X.getSign() + "] ")),
        INFO(Text.of(TextColors.GOLD, TextStyles.BOLD, "[!] ")),
        SUCCESS(Text.of(TextColors.GREEN, TextStyles.BOLD, "[" + AltCodes.CHECKMARK.getSign() + "] ")),
        ECO(Text.of(TextColors.GREEN, "[Eco] ")),
        DUEL(Text.of(TextColors.DARK_RED, "[Duel] ")),
        CHILD(null),
        ABILITY(Text.of(TextColors.AQUA, "[Ability] "));

        private Text text;

        Prefix(Text text){this.text = text;}

        public Text getText() {
            return text;
        }
    }

}
