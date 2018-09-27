package com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.Pair;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.cardbases.CardBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.Hotbars;
import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HotbarCardManipulate extends Hotbar {

    private CardBase cardBase;

    public HotbarCardManipulate(CardBase cardBase) {
        this.cardBase = cardBase;

        setupItems();
    }

    @Override
    protected void setupItems() {
        if(cardBase == null){
            return;
        }

        Pair<ItemStack, Callback> card;
        ItemStack item = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Right Click to use Card Ability"));

        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                //Activate card passive ability
            }

            @Override
            public void actionRightClick(Player player, HandType hand){

            }
        });
        addPair(1, card);

        item = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Card Description"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                displayDescription(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                displayDescription(player);
            }

            private void displayDescription(Player player){
                Message.Builder builder = Message.builder();
                builder.addReceiver(player);
                builder.addMessage(Text.of(" "));
                builder.addMessage(Text.of(TextColors.GRAY + "[===== " + cardBase.getName() + TextColors.GRAY + " =====]"));
                builder.addMessage(Text.of(" "));

                for(Text s: cardBase.getMesh().getValue(Keys.ITEM_LORE).get()){
                    builder.addAsChild(s, TextColors.GOLD);
                }
                builder.addMessage(Text.of(" "));

                builder.addMessage(Text.of(TextColors.GRAY + "[=====================================]"));
                Messager.sendMessage(builder.build());
            }
        });
        addPair(6, card);

        item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Return to Duel Menu"));
        card = new Pair<>(item, new Callback() {
            @Override
            public void actionLeftClick(Player player, HandType action) {
                loadMenu(player);
            }

            @Override
            public void actionRightClick(Player player, HandType hand){
                loadMenu(player);
            }

            private void loadMenu(Player player){
                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                PlayerUtils.getUserPlayer(player.getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
                PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get().setCurrentlyMovingCard(null);
            }
        });
        addPair(8, card);
    }

    @Override
    public void handleEmptyRightClick(Player player) {
        DuelUtils.displayCellInfo(player);
    }

    @Override
    public void handleEmptyLeftClick(Player player) {
        //If aiming at appropriate cell for the card to move to, move card
        CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get();
        Cell aim = cpp.getCurrentAim().get();
        CardBase card = cpp.getCurrentlyMovingCard();

        if(aim != null){
            if(card != null){
                CardMovement.MovementEntry entry = card.getMovement().getEntry(aim);

                if(entry.getType() == CardMovement.MovementEntry.MovementEntryType.EMPTY){
                    DuelUtils.moveCardToCell(player);
                    /*Cell old = card.getCurrentCell();
                    old.setAvailable(true);
                    aim.setOccupyingCard(card, false);
                    card.moveArmorStand(aim.getCenter(), old);*/
                } else {
                    //have the cards battle
                    ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager()
                            .findArenaWithCombatant(player.getUniqueId()).get().getGamemode().battle(card.getCurrentCell(), aim);
                }

                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                PlayerUtils.getUserPlayer(player.getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
                PlayerUtils.getCombatProfilePlayer(player.getUniqueId()).get().setCurrentlyMovingCard(null);
            }
        }
    }
}
