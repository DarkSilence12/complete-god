package com.bai.god.feature;

import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GodPlayerSnapshotHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("complete-god-snapshot");

    private GodPlayerSnapshotHelper() {
    }

    public static void snapshotCriticalFields(ServerPlayer player) {
        enforceGodInvariants(player);
    }

    public static void removeSnapshot(java.util.UUID uuid) {

    }

    public static void enforceGodInvariants(ServerPlayer player) {
        if (player == null) {
            return;
        }
        try {

            if (player.deathTime != 0) {
                player.deathTime = 0;
            }

            float max = player.getMaxHealth();
            if (player.isDeadOrDying() || player.getHealth() <= 0.0F) {
                player.setHealth(max);
            } else if (player.getHealth() > max) {
                player.setHealth(max);
            }

            if (player.hurtTime != 0) {
                player.hurtTime = 0;
            }
            if (player.hurtDuration != 0) {
                player.hurtDuration = 0;
            }
        } catch (Throwable t) {
            LOGGER.warn("维护神级玩家不变量时出错（玩家 {}）: {}",
                    player.getName().getString(), t.toString());
        }
    }
}
