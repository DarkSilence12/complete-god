package com.bai.god.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public final class GodMiningHelper {

    private GodMiningHelper() {
    }

    public static final int BUILT_IN_EFFICIENCY_LEVEL = 2;

    public static final float EFFICIENCY_2_BONUS = (float) (BUILT_IN_EFFICIENCY_LEVEL
            * BUILT_IN_EFFICIENCY_LEVEL + 1);

    public static boolean isInstantMiningActive(Player player) {
        return player != null && GodModeSystem.isGodPlayer(player);
    }

    public static float applyMining(Player player, BlockState state, BlockPos pos, float original) {
        if (player == null) {
            return original;
        }
        GodConfig.ConfigData cfg = GodConfig.getEffectiveConfig(player);
        if (cfg == null) {
            return original;
        }

        if (cfg.instantMining && isInstantMiningActive(player)) {
            return 1.0F;
        }

        if (cfg.miningSpeedBoost) {
            return applyBuiltInEfficiency(player, state, pos, original);
        }
        return original;
    }

    private static float applyBuiltInEfficiency(Player player, BlockState state, BlockPos pos,
                                                float original) {
        if (state == null) {
            return original;
        }
        return scaleForBuiltInEfficiency(player.getDestroySpeed(state), original);
    }

    public static float scaleForBuiltInEfficiency(float digSpeed, float original) {

        if (!(original > 0.0F) || Float.isInfinite(original)) {
            return original;
        }

        if (!(digSpeed > 0.0F)) {
            return original;
        }
        return original * ((digSpeed + EFFICIENCY_2_BONUS) / digSpeed);
    }
}
