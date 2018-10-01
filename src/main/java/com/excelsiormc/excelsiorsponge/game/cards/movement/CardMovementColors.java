package com.excelsiormc.excelsiorsponge.game.cards.movement;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;

public class CardMovementColors {

    public static final BlockState EMPTY = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.YELLOW).get();
    public static final BlockState ENEMY = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.RED).get();
    public static final BlockState TEAMMATE = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.GREEN).get();


}
