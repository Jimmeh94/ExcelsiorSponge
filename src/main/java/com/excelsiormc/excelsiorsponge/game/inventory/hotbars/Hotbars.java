package com.excelsiormc.excelsiorsponge.game.inventory.hotbars;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.inventory.Hotbar;
import com.excelsiormc.excelsiorsponge.game.inventory.hotbars.duel.*;

public class Hotbars {

    public static final HotbarActiveTurn HOTBAR_ACTIVE_TURN = new HotbarActiveTurn();
    public static final HotbarWaitingTurn HOTBAR_WAITING_TURN = new HotbarWaitingTurn();
    public static final HotbarDefault HOTBAR_DEFAULT = new HotbarDefault();
    public static final HotbarDuelOptionsCurrentTurn HOTBAR_DUEL_OPTIONS_CURRENT_TURN = new HotbarDuelOptionsCurrentTurn();
    public static final HotbarDuelOptionsWaitingTurn HOTBAR_DUEL_OPTIONS_WAITING_TURN = new HotbarDuelOptionsWaitingTurn();

    public static boolean isArenaDuelHotbar(Hotbar hotbar){
        return hotbar instanceof HotbarActiveTurn || hotbar instanceof HotbarCardManipulate
                || hotbar instanceof HotbarHand || hotbar instanceof HotbarWaitingTurn || hotbar instanceof HotbarDuelOptionsWaitingTurn
                || hotbar instanceof HotbarDuelOptionsCurrentTurn;
    }

}
