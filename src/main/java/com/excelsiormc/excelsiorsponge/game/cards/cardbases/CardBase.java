package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Message;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.StringUtils;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.components.actives.Active;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.cards.summon.SummonType;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.timers.DelayedOneUseTimer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class CardBase {

    public static final ItemType FACE_DOWN_CARD_MAT = ItemTypes.OBSIDIAN;

    protected CardDescriptor descriptor;
    protected StatHealth health;
    protected StatPower power;
    private List<Text> lore;
    private UUID owner;
    protected ArmorStand stand, position, cardFace;
    private ItemStack facedownItem;
    protected ItemStack faceupItem;
    protected Cell currentCell;
    protected CardMovement cardMovement;
    protected SummonType summonType;
    protected CardPosition cardPosition;
    protected CardFacePosition cardFacePosition = CardFacePosition.FACE_DOWN;
    protected Map<String, Text> extraDisplayInfo;
    protected Optional<Active> active = Optional.empty();
    protected List<CardKeys> cardKeys;

    public CardBase(UUID owner, CardDescriptor description, StatPower power, StatHealth health, CardMovement cardMovement,
                    SummonType summonType) {
        this.owner = owner;
        this.descriptor = description;
        this.power = power;
        this.health = health;
        this.cardMovement = cardMovement;
        this.cardMovement.setOwner(this);

        if(descriptor.item != null){
            generateItemStack();
        }

        if(summonType != null) {
            this.summonType = summonType;
            this.summonType.setOwner(this);
        }

        extraDisplayInfo = new HashMap<>();
        cardKeys = new CopyOnWriteArrayList<>();
    }

    //This is for effects on summon
    protected void summoned(){}

    public void handleSummon(){
        if(summonType != null && summonType.canSummon()){
            DuelEvent.CardEvent.CardPlacePre event = new DuelEvent.CardEvent.CardPlacePre(ExcelsiorSponge.getServerCause(), this);
            Sponge.getEventManager().post(event);

            if(!event.isCancelled()) {
                summonType.summon();
                summoned();
            }

            Sponge.getEventManager().post(new DuelEvent.CardEvent.CardPlacePost(ExcelsiorSponge.getServerCause(), this));
        }
    }

    public void addKey(CardKeys key){
        if(!cardKeys.contains(key)){
            cardKeys.add(key);
        }
    }

    public boolean hasKey(CardKeys key){
        return cardKeys.contains(key);
    }

    public void removeKey(CardKeys key){
        cardKeys.remove(key);
    }

    public Optional<Active> getActive() {
        return active;
    }

    public void addExtraDisplayInfo(String id, Text text){
        extraDisplayInfo.put(id, text);
    }

    public void removeExtraDisplayInfo(String id){
        extraDisplayInfo.remove(id);
    }

    public Map<String, Text> getExtraDisplayInfo() {
        return extraDisplayInfo;
    }

    public CardDescriptor getDescriptor() {
        return descriptor;
    }

    protected List<Text> generateLore() {
        List<Text> give = new ArrayList<>();
        give.add(descriptor.getType());
        give.add(descriptor.rarityText);
        give.add(descriptor.getMovement());
        give.add(descriptor.getSummon());
        give.add(Text.of( " "));
        give.addAll(StringUtils.getLongTextAsShort(descriptor.getDescription()));
        give.add(Text.of( " "));
        give.add(health.getFullDisplay());
        give.add(power.getFullDisplay());
        return give;
    }

    public void updateLore(){
        if(facedownItem != null) {
            facedownItem.offer(Keys.ITEM_LORE, generateLore());
        }
        if(faceupItem != null) {
            faceupItem.offer(Keys.ITEM_LORE, generateLore());
        }
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
            stand.setHelmet(faceupItem);
            stand.offer(Keys.CUSTOM_NAME_VISIBLE, true);

            Sponge.getEventManager().post(new DuelEvent.CardEvent.CardFlipped(ExcelsiorSponge.getServerCause(), this));
        }
    }

    public void displayAvailableSpotsToMoveTo(){
        cardMovement.displayAvailableSpotsToMoveTo();
    }

    public void displayStats(Player displayTo){
        Message.Builder builder = Message.builder();
        builder.addReceiver(displayTo);

        builder.addMessage(Text.of(TextColors.GRAY, "[-=======================================-]"));
        builder.addMessage(getName());
        builder.append(getLoreAsMessage());
        builder.addMessage(Text.of(" "));
        builder.addMessage(health.getFullDisplay());
        builder.addMessage(power.getFullDisplay());
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
        lore = generateLore();

        faceupItem = ItemStack.builder().itemType(descriptor.getMat()).build();
        faceupItem.offer(Keys.ITEM_DURABILITY, descriptor.getMatDamageValue());
        faceupItem.offer(Keys.ITEM_LORE, lore);
        faceupItem.offer(Keys.DISPLAY_NAME, descriptor.getName());

        facedownItem = ItemStack.builder().itemType(FACE_DOWN_CARD_MAT).build();
        facedownItem.offer(Keys.ITEM_DURABILITY, descriptor.getMatDamageValue());
        facedownItem.offer(Keys.ITEM_LORE, lore);
        facedownItem.offer(Keys.DISPLAY_NAME, Text.of(""));
    }

    public ItemStack getFacedownItem() {
        return facedownItem;
    }

    public ItemStack getFaceupItem() {
        return faceupItem;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwnerPlayer(){
        return PlayerUtils.getPlayer(owner).isPresent();
    }

    public Text getName() {
        return descriptor.getName();
    }

    public void spawn(Location center) {
        cardPosition = CardPosition.ATTACK;

        stand = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition());
        stand.offer(Keys.DISPLAY_NAME, descriptor.getName());
        stand.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
        stand.setHelmet(cardFacePosition == CardFacePosition.FACE_UP ? faceupItem : facedownItem);
        stand.offer(Keys.INVISIBLE, true);
        stand.offer(Keys.CUSTOM_NAME_VISIBLE, cardFacePosition == CardFacePosition.FACE_UP);
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

    public void spawn(){
        if(currentCell != null) {
            spawn(new Location(Sponge.getServer().getWorld(currentCell.getWorld()).get(), currentCell.getCenterCeiling()));
        }
    }

    public void remove(){
        if(stand != null){
            stand.remove();
        }
        if(position != null){
            position.remove();
        }
        if(cardFace != null){
            cardFace.remove();
        }
    }

    public void changePosition(CardPosition cardPosition){
        this.cardPosition = cardPosition;
        position.offer(Keys.DISPLAY_NAME, cardPosition.getText());
        if(isOwnerPlayer()){
            Messager.sendMessage(PlayerUtils.getPlayer(owner).get(), Text.builder().append(getName())
                    .append(Text.of(TextColors.GRAY, " has been switched to the "
                            + StringUtils.enumToString(cardPosition, true) + " position!")).build(),
                    Messager.Prefix.DUEL);
        }
    }

    public void move(Vector3d destination, Cell old){
        if(cardPosition == CardPosition.DEFENSE){
            changePosition(CardPosition.ATTACK);
        }

        stand.setLocation(new Location<World>(stand.getWorld(), destination.getX(), destination.getY(), destination.getZ()));

        new DelayedOneUseTimer(10L) {
            @Override
            protected void runTask() {
                cardMovement.clearCurrentlyHighlighted();
            }
        };

        cardMovement.setCanMoveThisTurn(false);
        Sponge.getEventManager().post(new DuelEvent.CardEvent.CardMoved(ExcelsiorSponge.getServerCause(), this, old, currentCell));
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

        Sponge.getEventManager().post(new DuelEvent.CardEvent.CardDestroyed(ExcelsiorSponge.getServerCause(), this));
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
            changePosition(CardPosition.ATTACK);
        }
    }

    public boolean isFaceDown() {
        return cardFacePosition == CardFacePosition.FACE_DOWN;
    }

    public boolean hasStat(StatBase stat) {
        return stat == health || stat == power;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public List<Text> getLore() {
        return faceupItem.get(Keys.ITEM_LORE).get();
    }

    public boolean canSummon() {
        return summonType.canSummon();
    }

    public SummonType getSummonType() {
        return summonType;
    }

    public enum CardRarity {
        ULTRA_RARE(TextColors.GOLD),
        SUPER_RARE(TextColors.LIGHT_PURPLE),
        RARE(TextColors.GREEN),
        COMMON(TextColors.GRAY);

        private TextColor color;

        CardRarity(TextColor color) {
            this.color = color;
        }

        public Text getText(){
            return Text.of(color, StringUtils.enumToString(this, true));
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

    public enum CardKeys {
        DONT_DRAW_CELL_FOR_ENEMY;
    }
}
