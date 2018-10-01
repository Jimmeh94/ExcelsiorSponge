package com.excelsiormc.excelsiorsponge.utils;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;

public class BlockStateColors {

    public static final BlockState EMPTY = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.YELLOW).get();
    public static final BlockState ENEMY_THREAT = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.RED).get();
    public static final BlockState OWNER = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.LIME).get();
    public static final BlockState TEAMMATE = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.CYAN).get();
    public static final BlockState ENEMY_NO_CURRENT_THREAT = BlockState.builder().blockType(BlockTypes.STAINED_GLASS).build().with(Keys.DYE_COLOR, DyeColors.ORANGE).get();


}
