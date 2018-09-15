package com.excelsiormc.excelsiorsponge.game.cards;

import com.excelsiormc.excelsiorsponge.game.match.field.Cell;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.List;
import java.util.UUID;

public abstract class CardBase {

    private double level;
    private String name;
    private UUID owner;
    private ArmorStand stand;
    private ItemType material;
    private int materialDamageValue;
    private ItemStack mesh;
    private Cell currentCell;
    private CardMovement cardMovement;

    public CardBase(UUID owner, double level, String name, ItemType material, int materialDamageValue, CardMovement cardMovement) {
        this.owner = owner;
        this.level = level;
        this.name = name;
        this.material = material;
        this.materialDamageValue = materialDamageValue;
        this.cardMovement = cardMovement;
        this.cardMovement.setOwner(this);

        generateItemStack();
    }

    protected abstract List<Text> generateLore();

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void generateItemStack(){
        mesh = ItemStack.builder().itemType(material).build();
        mesh.offer(Keys.ITEM_DURABILITY, materialDamageValue);
        mesh.offer(Keys.ITEM_LORE, generateLore());
        mesh.offer(Keys.DISPLAY_NAME, Text.of(name));
    }

    public ItemStack getMesh() {
        return mesh;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwnerPlayer(){
        return Sponge.getServer().getPlayer(owner).isPresent();
    }

    public double getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    /**
     * This will display the 3D view in front of the card owner with a book/chat with details of the description
     * @param location
     */
    public void displayCardDescription(Location location){
        /*Player player = Bukkit.getPlayer(owner);
        if(player == null){
            return;
        }

        if(clientArmorStand != null && clientArmorStand.getFirst() != null){
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(clientArmorStand.getFirst().getId());
            NMSUtils.getPlayerConnection(player).sendPacket(packet);
        }

        description = new EntityArmorStand(((CraftWorld)location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        description.setInvisible(true);
        description.setBasePlate(false);
        //description.setCustomName(name);
        description.setCustomNameVisible(true);

        clientArmorStand = new Pair<>(description, Arrays.asList(owner));

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(clientArmorStand.getFirst());
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(clientArmorStand.getFirst().getId(), EnumItemSlot.HEAD, NMSUtils.getNMSCopy(mesh));

        PlayerConnection connection = NMSUtils.getPlayerConnection(player);
        connection.sendPacket(packet);
        connection.sendPacket(equipment);

        PlayerUtils.getCombatProfilePlayer(owner).get().setViewingClientArmorstand(new CombatantProfilePlayer.ViewingClientArmorstand(clientArmorStand.getFirst(), owner));*/
    }

    public void spawn3DRepresentationServer(Location center) {
        stand = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition());
        stand.offer(Keys.DISPLAY_NAME, Text.of(name));
        stand.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
        stand.setHelmet(ItemStack.builder().itemType(material).build());
        stand.offer(Keys.INVISIBLE, true);
        stand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        stand.offer(Keys.HAS_GRAVITY, false);
        center.getExtent().spawnEntity(stand);
    }

    public void removeArmorStand(){
        if(stand != null){
            stand.remove();
        }
    }

    public void moveArmorStand(Vector3d direction){
        stand.setVelocity(direction.mul(2));
    }

    public CardMovement getMovement() {
        return cardMovement;
    }

    public boolean isOwner(UUID uniqueId) {
        return uniqueId.compareTo(owner) == 0;
    }
}
