package com.excelsiormc.excelsiorsponge.excelsiorcore.services.economy;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import org.spongepowered.api.Sponge;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Economy {

    private List<Account> accounts;
    private List<Currency> currencies;
    private Map<Currency, Double> defaults;

    public Economy() {
        accounts = new CopyOnWriteArrayList<>();
        currencies = new ArrayList<>();
        defaults = new HashMap<>();
    }

    public void addCurrency(Currency currency){
        currencies.add(currency);
    }

    public void addAccount(UUID uuid){
        Account account = new Account(uuid);
        applyDefaults(account);
        accounts.add(account);
    }

    public void addDefault(Currency currency, double amount){
        defaults.put(currency, amount);
    }

    protected void applyDefaults(Account account){
        for(Map.Entry<Currency, Double> entry: defaults.entrySet()){
            account.addCurrency(entry.getKey(), entry.getValue());
        }
    }

    protected Optional<Currency> getCurrency(Currency currency){
        for(Currency c: currencies){
            if(c.equals(currency)){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public double getBalance(UUID uuid, Currency currency){
        Optional<Currency> c = getCurrency(currency);
        if(c.isPresent()){
            return getOrCreateAccount(uuid).get().getBalance(currency);
        }
        return 0;
    }

    public boolean withdraw(UUID uuid, Currency currency, double amount){
        if(currencies.contains(currency)){
            if(getOrCreateAccount(uuid).get().withdraw(currency, amount)){
                return true;
            }
        }
        return false;
    }

    public void deposit(UUID uuid, Currency currency, double amount){
        if(currencies.contains(currency)){
            getOrCreateAccount(uuid).get().deposit(currency, amount);
        }
    }

    public void payPlayer(UUID payer, UUID receiver, Currency currency, double amount){
        if(currencies.contains(currency)){
            Account p = getOrCreateAccount(payer).get();
            Account r = getOrCreateAccount(receiver).get();
            if(p.withdraw(currency, amount)){
                r.deposit(currency, amount);
                Messager.sendEconomyPayMessage(Sponge.getServer().getPlayer(payer).get(), Sponge.getServer().getPlayer(receiver).get(), currency, amount);
            }
        }
    }

    public Optional<Account> getOrCreateAccount(UUID uuid){
        if(!hasAccount(uuid)){
            addAccount(uuid);
        }
        for(Account account: accounts){
            if(account.isOwner(uuid)){
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    public boolean hasAccount(UUID uuid){
        for(Account account: accounts){
            if(account.isOwner(uuid)){
                return true;
            }
        }
        return false;
    }
}
