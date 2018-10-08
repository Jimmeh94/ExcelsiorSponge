package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.timers.AbstractTimer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CardBase {

    private double level;
    protected StatHealth health;
    protected StatPower power;
    private Text name;
    private List<Text> lore;
    private UUID owner;
    protected ArmorStand stand, position, cardFace;
    private ItemType material;
    private int materialDamageValue;
    private ItemStack mesh;
    protected Cell currentCell;
    protected CardMovement cardMovement;
    protected CardRarity rarity;
    protected SummonType summonType;
    protected CardPosition cardPosition;
    protected CardFacePosition cardFacePosition = CardFacePosition.FACE_DOWN;

    public CardBase(UUID owner, double level, String name, CardRarity rarity, StatPower power, StatHealth health,
                    ItemType material, int materialDamageValue, CardMovement cardMovement, SummonType summonType) {
        this.owner = owner;
        this.level = level;
        this.name = Text.of(rarity.getColor(), name);
        this.rarity = rarity;
        this.power = power;
        this.health = health;
        this.material = material;
        this.materialDamageValue = materialDamageValue;
        this.cardMovement = cardMovement;
        this.cardMovement.setOwner(this);

        if(summonType != null) {
            this.summonType = summonType;
            this.summonType.setOwner(this);
        }

        if(material != null) {
            generateItemStack();
        }
    }

    public void handleSummon(){
        if(summonType != null && summonType.canSummon()){
            summonType.summon();
        }
    }

    protected abstract Text getCardDescription();

    protected List<Text> generateLore() {
        List<Text> give = new ArrayList<>();
        give.add(Text.builder().append(Text.of(TextColors.GRAY, "Rarity: "), rarity.getText()).build());
        give.add(Text.of(TextColors.GRAY, "Level: 1"));
        give.add(Text.of( " "));
        give.add(getCardDescription());
        return give;
    }

    public void setCardFacePosition(CardFacePosition cardFacePosition) {
        this.cardFacePosition = cardFacePosition;
    }

    public CardFacePosition getCardFacePosition() {
        return cardFacePosition;
    }

    public void flipCard(){
        if(cardFacePosition == CardFacePosition.FACE_DOWN){
            cardFacePosition = CardFacePosition.FACE_UP;
            cardFace.offer(Keys.DISPLAY_NAME, cardFacePosition.getText());

            Sponge.getEventManager().post(new DuelEvent.CardFlipped(ExcelsiorSponge.getServerCause(), this));
        }
    }

    public void displayAvailableSpotsToMoveTo(){
        cardMovement.displayAvailableSpotsToMoveTo();
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public void displayStats(Player displayTo){
        Message.Builder builder = Message.builder();
        builder.addReceiver(displayTo);

        builder.addMessage(Text.of(TextColors.GRAY, "[-=======================================-]"));
        builder.addMessage(getName());
        builder.append(getLoreAsMessage());
        builder.addMessage(Text.of(" "));
        builder.addMessage(Text.of(TextColors.RED, "Health: " + health.getCurrent()));
        builder.addMessage(Text.of(TextColors.GRAY, "Power: " + power.getCurrent()));
        builder.addMessage(Text.of(TextColors.GRAY, "[-=======================================-]"));
        Messager.sendMessage(builder.build());
    }

    public StatBase getPower() {
        return power;
    }

    public StatBase getHealth() {
        return health;
    }

    protected Message getLoreAsMessage(){
        Message.Builder builder = Message.builder();
        for(Text text: lore){
            builder.addAsChild(text, TextColors.GOLD);
        }
        return builder.build();
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void generateItemStack(){
        mesh = ItemStack.builder().itemType(material).build();
        mesh.offer(Keys.ITEM_DURABILITY, materialDamageValue);

        lore = generateLore();
        mesh.offer(Keys.ITEM_LORE, lore);

        mesh.offer(Keys.DISPLAY_NAME, Text.of(name));
    }

    public ItemStack getMesh() {
        return mesh;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwnerPlayer(){
        return PlayerUtils.getPlayer(owner).isPresent();
    }

    public double getLevel() {
        return level;
    }

    public Text getName() {
        return name;
    }

    public void spawn(Location center) {
        cardPosition = CardPosition.ATTACK;

        stand = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition());
        stand.offer(Keys.DISPLAY_NAME, Text.of(name));
        stand.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
        stand.setHelmet(ItemStack.builder().itemType(material).build());
        stand.offer(Keys.INVISIBLE, true);
        stand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        stand.offer(Keys.HAS_GRAVITY, false);
        center.getExtent().spawnEntity(stand);

        cardFace = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition().add(0, 1, 0));
        cardFace.offer(Keys.DISPLAY_NAME, cardFacePosition.getText());
        cardFace.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
        cardFace.offer(Keys.INVISIBLE, true);
        cardFace.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        cardFace.offer(Keys.HAS_GRAVITY, false);
        center.getExtent().spawnEntity(cardFace);
        stand.addPassenger(cardFace);

        position = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition().add(0, 1, 0));
        position.offer(Keys.DISPLAY_NAME, cardPosition.getText());
        position.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
        position.offer(Keys.INVISIBLE, true);
        position.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        position.offer(Keys.HAS_GRAVITY, false);
        position.offer(Keys.ARMOR_STAND_IS_SMALL, true);
        center.getExtent().spawnEntity(position);
        cardFace.addPassenger(position);
    }

    public void remove(){
        if(stand != null){
            stand.remove();
        }
        if(position != null){
            position.remove();
        }
    }

    public void changePosition(CardPosition cardPosition){
        this.cardPosition = cardPosition;
        position.offer(Keys.DISPLAY_NAME, cardPosition.getText());
    }

    public void move(Vector3d destination, Cell old){
        stand.setLocation(new Location<World>(stand.getWorld(), destination.getX(), destination.getY(), destination.getZ()));

        ExcelsiorSponge.INSTANCE.getDirectionalAimArenaTimer().addDelayedTask(new AbstractTimer.DelayedTask(1) {
            @Override
            public void doTask() {
                cardMovement.clearCurrentlyHighlighted();
            }
        });
        cardMovement.setCanMoveThisTurn(false);

        Sponge.getEventManager().post(new DuelEvent.CardMoved(ExcelsiorSponge.getServerCause(), this, old, currentCell));
    }

    public CardMovement getMovement() {
        return cardMovement;
    }

    public boolean isOwner(UUID uniqueId) {
        return uniqueId.compareTo(owner) == 0;
    }

    public void cardEliminated(){
        remove();
        currentCell.setAvailable(true);
        currentCell = null;

        if(isOwnerPlayer()){
            Messager.sendMessage(PlayerUtils.getPlayer(owner).get(),
                    Text.builder().append(getName(), Text.of(TextColors.RED, " has been destroyed!")).build(), Messager.Prefix.DUEL);
        }

        DuelUtils.getCombatProfilePlayer(owner).get().addToGraveyard(this);

        Sponge.getEventManager().post(new DuelEvent.CardDestroyed(ExcelsiorSponge.getServerCause(), this));
    }

    public void clearCurrentlyHighlighted() {
        cardMovement.clearCurrentlyHighlighted();
    }

    public boolean generateSpots() {
        return cardMovement.generateSpots();
    }

    public void subtractHealth(double damage) {
        health.subtract(damage);
    }

    public void toggleCardPosition() {
        if(cardPosition == CardPosition.ATTACK){
            changePosition(CardPosition.DEFENSE);
        } else {
            flipCard();
        }
    }

    public boolean isFaceDown() {
        return cardFacePosition == CardFacePosition.FACE_DOWN;
    }

    public enum CardRarity {
        LEGENDARY(TextColors.GOLD),
        RARE(TextColors.LIGHT_PURPLE),
        UNCOMMON(TextColors.AQUA),
        ENHANCED(TextColors.GREEN),
        COMMON(TextColors.GRAY);

        private TextColor color;

        CardRarity(TextColor color) {
            this.color = color;
        }

        public Text getText(){
            return Text.of(color, StringUtils.capitalizeFirstLetter(this.toString()));
        }

        public TextColor getColor() {
            return color;
        }
    }

    public enum CardPosition {
        ATTACK(Text.of(TextColors.RED, TextStyles.BOLD, "Attacking")),
        DEFENSE(Text.of(TextColors.GRAY, TextStyles.BOLD, "Defending"));

        private Text text;

        CardPosition(Text text) {
            this.text = text;
        }

        public Text getText() {
            return text;
        }
    }

    public enum CardFacePosition {
        FACE_DOWN(Text.of(TextColors.YELLOW, TextStyles.BOLD, "Face Down")),
        FACE_UP(Text.of(TextColors.YELLOW, TextStyles.BOLD, "Face Up"));

        private Text text;

        CardFacePosition(Text text) {
            this.text = text;
        }

        public Text getText() {
            return text;
        }
    }
}
