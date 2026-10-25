package com.bai.god.feature;

import com.bai.god.God;
import net.minecraft.server.level.ServerPlayer;

public final class GodPlayerJoinHandler {

    private GodPlayerJoinHandler() {
    }

    public static void initialize() {
        God.LOGGER.info("神级玩家加入处理器已初始化");
    }

    public static void handlePlayerJoin(ServerPlayer player) {
        if (player == null) {
            return;
        }
        if (GodConfig.isGodModeAutoOnJoinEnabled()) {
            GodModeSystem.activateGodModeSilent(player);
            God.LOGGER.info("「进服自动成神」已开启，玩家 {} 自动获得神级状态",
                    player.getName().getString());
        } else {

            GodModeSystem.syncGodStateToClient(player);
        }
    }

    public static void handlePlayerClone(ServerPlayer player, boolean wasDeath) {
        if (player == null) {
            return;
        }
        GodModeSystem.syncGodStateToClient(player);
    }
}
