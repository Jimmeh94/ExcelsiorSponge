package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcards.CardDescriptor;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatHealth;
import com.excelsiormc.excelsiorsponge.game.cards.stats.StatPower;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.timers.DelayedOneUseTimer;
import com.excelsiormc.excelsiorsponge.utils.DuelUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class CardBaseAvatar extends CardBase {

    private Human human;
    private CombatantProfile owner;
    private ArmorStand name;
    private List<Cell> placeableCells;

    public CardBaseAvatar(UUID owner, CombatantProfile profile, StatHealth health, CardMovement cardMovement) {
        super(owner, new AvatarCardDescriptor((int) health.getCurrent()), new StatPower(0, 0), health,
                cardMovement, null);

        this.owner = profile;
        placeableCells = new CopyOnWriteArrayList<>();
    }

    public void generateAndHighlightPlaceableCells(Player player){
        placeableCells.clear();
        Grid grid = DuelUtils.getArena(owner.getUUID()).get().getGrid();
        placeableCells = grid.getSquareGroupofCells(currentCell, 1);
        for(Cell cell: placeableCells){
            if(!cell.isAvailable()){
                placeableCells.remove(cell);
            } else {
                cell.drawCustomEmpty(player);
            }
        }
    }

    @Override
    public void entityFace(Vector3d location) {
        human.lookAt(location);
    }

    public boolean isPlaceable(Cell target){
        return placeableCells.contains(target);
    }

    public void removePlaceableCells(Player player){
        for(Cell cell: placeableCells){
            cell.resetClientView(player);
        }
        placeableCells.clear();
    }

    @Override
    public void remove(){
        if(stand != null){
            stand.remove();
        }
        if(name != null){
            name.remove();
        }
        if(human != null){
            human.remove();
        }
    }

    @Override
    public void toggleCardPosition() {

    }

    @Override
    public void spawn(Location center) {
        if(owner.isPlayer()) {
            human = (Human) center.getExtent().createEntity(EntityTypes.HUMAN, center.getPosition());
            human.offer(Keys.HAS_GRAVITY, false);
            human.offer(Keys.SKIN_UNIQUE_ID, owner.getPlayer().getUniqueId());
            center.getExtent().spawnEntity(human);

            stand = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition());
            stand.offer(Keys.DISPLAY_NAME, Text.of(TextColors.RED, (int)getHealth().getCurrent()));
            stand.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
            stand.offer(Keys.INVISIBLE, true);
            stand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
            stand.offer(Keys.HAS_GRAVITY, false);
            stand.offer(Keys.ARMOR_STAND_IS_SMALL, true);
            center.getExtent().spawnEntity(stand);

            name = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition());
            name.offer(Keys.DISPLAY_NAME, owner.getPlayer().getDisplayNameData().displayName().get());
            name.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
            name.offer(Keys.INVISIBLE, true);
            name.offer(Keys.CUSTOM_NAME_VISIBLE, true);
            name.offer(Keys.HAS_GRAVITY, false);
            name.offer(Keys.ARMOR_STAND_IS_SMALL, true);
            center.getExtent().spawnEntity(name);

            human.addPassenger(stand);
            stand.addPassenger(name);
        }
    }

    @Override
    public void move(Vector3d destination, Cell old){
        human.setLocation(new Location<World>(human.getWorld(), destination.getX(), destination.getY(), destination.getZ()));

        new DelayedOneUseTimer(10L) {
            @Override
            protected void runTask() {
                cardMovement.clearCurrentlyHighlighted();
            }
        };

        cardMovement.setCanMoveThisTurn(false);
        Sponge.getEventManager().post(new DuelEvent.CardEvent.CardMoved(ExcelsiorSponge.getServerCause(), this, old, currentCell));
    }

    @Override
    public void subtractHealth(double damage) {
        super.subtractHealth(damage);

        stand.offer(Keys.DISPLAY_NAME, Text.of(""));
        stand.offer(Keys.DISPLAY_NAME, Text.of(TextColors.RED, (int)getHealth().getCurrent()));

        if(owner.isPlayer()) {
            Messager.sendMessage(owner.getPlayer(), Text.of(TextColors.RED, "Your avatar took " + damage + " damage!"), Messager.Prefix.DUEL);
        }
    }

    protected static class AvatarCardDescriptor extends CardDescriptor{

        public AvatarCardDescriptor(int baseHealth) {
            super("", "", CardType.AVATAR, "", "", baseHealth, 0, null, null, 0);
        }

        @Override
        public Text getDescription() {
            return Text.of();
        }
    }
}
