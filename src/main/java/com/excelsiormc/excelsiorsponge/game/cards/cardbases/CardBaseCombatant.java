package com.excelsiormc.excelsiorsponge.game.cards.cardbases;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.events.custom.DuelEvent;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.user.stats.StatBase;
import com.excelsiormc.excelsiorsponge.game.cards.movement.CardMovement;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.Cell;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfile;
import com.excelsiormc.excelsiorsponge.game.user.StatIDs;
import com.excelsiormc.excelsiorsponge.timers.AbstractTimer;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class CardBaseCombatant extends CardBase {

    private Human human;
    private CombatantProfile owner;
    private ArmorStand name;

    public CardBaseCombatant(UUID owner, CombatantProfile profile, double level, CardMovement cardMovement) {
        super(owner, level, "", CardRarity.LEGENDARY, null, 0, cardMovement);

        this.owner = profile;
    }

    @Override
    protected void generateStats() {
        stats.addStat(StatIDs.HEALTH, new StatBase(8000, 8000, Text.of(TextColors.RED, "Health")));
        stats.addStat(StatIDs.ENERGY, new StatBase(1000, 1000, Text.of(TextColors.RED, "Energy")));
    }

    @Override
    protected Text getCardDescription() {
        return Text.of();
    }

    @Override
    public void spawn(Location center) {
        if(owner.isPlayer()) {
            human = (Human) center.getExtent().createEntity(EntityTypes.HUMAN, center.getPosition());
            human.offer(Keys.HAS_GRAVITY, false);
            human.offer(Keys.SKIN_UNIQUE_ID, owner.getPlayer().getUniqueId());
            center.getExtent().spawnEntity(human);

            stand = (ArmorStand) center.getExtent().createEntity(EntityTypes.ARMOR_STAND, center.getPosition());
            stand.offer(Keys.DISPLAY_NAME, Text.of(TextColors.RED, (int)stats.getStat(StatIDs.HEALTH).get().getCurrent()));
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

        ExcelsiorSponge.INSTANCE.getDirectionalAimArenaTimer().addDelayedTask(new AbstractTimer.DelayedTask(1) {
            @Override
            public void doTask() {
                cardMovement.clearCurrentlyHighlighted();
            }
        });
        cardMovement.setCanMoveThisTurn(false);

        Sponge.getEventManager().post(new DuelEvent.CardMoved(ExcelsiorSponge.getServerCause(), this, old, currentCell));
    }
}