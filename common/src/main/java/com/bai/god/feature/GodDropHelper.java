package com.bai.god.feature;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GodDropHelper {

    private GodDropHelper() {
    }

    public static final int DROP_MULTIPLIER = 3;

    public static final float LUCKY_CHANCE = 0.5F;

    public static final int LUCKY_MIN_FACTOR = 2;

    public static final int LUCKY_MAX_FACTOR = 3;

    public static List<ItemStack> lucky(List<ItemStack> original, RandomSource random) {
        if (original == null || original.isEmpty() || random == null) {
            return original;
        }

        if (random.nextFloat() >= LUCKY_CHANCE) {
            return original;
        }
        int factor = LUCKY_MIN_FACTOR + random.nextInt(LUCKY_MAX_FACTOR - LUCKY_MIN_FACTOR + 1);
        return multiply(original, factor);
    }

    public static List<ItemStack> triple(List<ItemStack> original) {
        return multiply(original, DROP_MULTIPLIER);
    }

    private static List<ItemStack> multiply(List<ItemStack> original, int factor) {
        if (original == null || original.isEmpty() || factor <= 1) {
            return original;
        }
        List<ItemStack> out = new ArrayList<>(original.size());
        for (ItemStack drop : original) {
            if (drop == null || drop.isEmpty()) {
                continue;
            }
            ItemStack copy = drop.copy();
            int max = Math.max(1, copy.getMaxStackSize());
            copy.setCount(Math.min(max, copy.getCount() * factor));
            out.add(copy);
        }
        return out;
    }

    private static final Map<ResourceKey<Level>, ServerPlayer> LAST_MINER = new ConcurrentHashMap<>();

    private static final Map<ResourceKey<Level>, Long> LAST_MINER_TICK = new ConcurrentHashMap<>();

    private static final long MINER_WINDOW_TICKS = 1L;

    public static void noteMiner(ServerPlayer player) {
        if (player == null || player.level() == null) {
            return;
        }
        try {
            ResourceKey<Level> dim = player.level().dimension();
            LAST_MINER.put(dim, player);
            LAST_MINER_TICK.put(dim, player.level().getGameTime());
        } catch (Throwable ignored) {

        }
    }

    public static ServerPlayer currentMiner(ServerLevel level) {
        if (level == null) {
            return null;
        }
        try {
            ResourceKey<Level> dim = level.dimension();
            ServerPlayer player = LAST_MINER.get(dim);
            Long tick = LAST_MINER_TICK.get(dim);
            if (player == null || tick == null || player.isRemoved()) {
                return null;
            }
            if (level.getGameTime() - tick > MINER_WINDOW_TICKS) {
                return null;
            }
            return player;
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static boolean isBedrock(BlockState state) {
        return state != null && state.is(Blocks.BEDROCK);
    }

    public static List<ItemStack> bedrockDrops() {
        List<ItemStack> out = new ArrayList<>(1);
        out.add(new ItemStack(Blocks.BEDROCK));
        return out;
    }
}
