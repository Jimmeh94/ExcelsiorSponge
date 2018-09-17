package com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Account {

    private Map<Currency, Double> balance;
    private UUID owner;

    public Account(UUID owner) {
        this.owner = owner;
        balance = new HashMap<>();
    }

    public void addCurrency(Currency currency, double amount){
        if(!balance.containsKey(currency)){
            balance.put(currency, amount);
        }
    }

    public Text getDisplayName() {
        return Text.of(TextColors.GREEN, Sponge.getServer().getPlayer(owner).get().getDisplayNameData().displayName().get().toPlain() + "'s Account: ");
    }


    public boolean isOwner(UUID uuid) {
        return uuid.compareTo(owner) == 0;
    }

    public double getBalance(Currency currency){
        return balance.containsKey(currency) ? balance.get(currency) : 0;
    }

    protected boolean hasCurrency(Currency currency){
        return balance.containsKey(currency);
    }

    public void deposit(Currency currency, double amount){
        balance.put(currency, balance.get(currency) + amount);
        displayBalance(currency);
    }

    public boolean withdraw(Currency currency, double amount){
        if(!hasCurrency(currency) || getBalance(currency) < amount){
            Messager.sendEconomyCantAffordMessage( this, currency, amount);
            return false;
        }

        balance.put(currency, balance.get(currency) - amount);
        displayBalance(currency);
        return true;
    }

    public void displayAllBalances(){
        Message.Builder builder = Message.builder().addReceiver(Sponge.getServer().getPlayer(owner).get())
                .addMessage(Text.of(TextColors.GREEN, "Your account balances: "), Messager.Prefix.ECO);
        for(Map.Entry<Currency, Double> entry: balance.entrySet()){
            builder.addAsChild(Text.of(entry.getKey().getSymbol().toPlain() + entry.getValue()), TextColors.GOLD);
        }
        Messager.sendMessage(builder.build());
    }

    public void displayBalance(Currency currency){
        Messager.sendMessage(Sponge.getServer().getPlayer(owner).get(), Text.of(TextColors.GREEN, "Current " + currency.getDisplayName().toPlain()
             + " balance: " + currency.getSymbol().toPlain() + balance.get(currency)));
    }

    public UUID getOwner() {
        return owner;
    }
}
