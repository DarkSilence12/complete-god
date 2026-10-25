package com.bai.god.feature;

import com.bai.god.God;
import com.bai.god.network.GodNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GodModeSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger("god-mode");

    private static final Map<UUID, Boolean> GOD_MODE_STATES = new ConcurrentHashMap<>();

    private static volatile MinecraftServer currentServer;

    public static void initialize() {
        GOD_MODE_STATES.clear();
        LOGGER.info("超越一切的存在系统已初始化（神级状态仅由手动 /god on 开启）");
    }

    public static void setServer(MinecraftServer server) {
        currentServer = server;
    }

    public static void clearServer() {
        currentServer = null;
    }

    private static List<ServerPlayer> onlinePlayers() {
        MinecraftServer server = currentServer;
        if (server == null) {
            return List.of();
        }
        return new ArrayList<>(server.getPlayerList().getPlayers());
    }

    public static boolean isGodPlayer(Player player) {
        return player != null && isGodPlayerByUuid(player.getUUID());
    }

    public static boolean isGodPlayerByUuid(UUID uuid) {
        return uuid != null && GOD_MODE_STATES.getOrDefault(uuid, false);
    }

    public static boolean isGodPlayerUuid(UUID uuid) {
        return isGodPlayerByUuid(uuid);
    }

    public static boolean isInGodPlayerList(ServerPlayer player) {
        return player != null;
    }

    public static boolean isGodModeActive() {
        return GOD_MODE_STATES.values().stream().anyMatch(Boolean::booleanValue);
    }

    public static void setClientGodState(UUID uuid, boolean isGod) {
        if (uuid == null) {
            return;
        }
        GOD_MODE_STATES.put(uuid, isGod);
    }

    public static void activateGodMode(ServerPlayer player) {
        if (!isInGodPlayerList(player)) {
            return;
        }

        UUID playerUuid = player.getUUID();
        if (isGodPlayer(player)) {

            syncGodStateToClient(player);
            return;
        }
        GOD_MODE_STATES.put(playerUuid, true);

        persistGodState(player, playerUuid, true);

        GodPlayerSnapshotHelper.snapshotCriticalFields(player);

        player.displayClientMessage(
                Component.literal("§6你已超越世间一切神明，成为至高无上的存在")
                        .withStyle(ChatFormatting.GOLD), true);

        HiddenAchievementSystem.giveBeyondEverythingAchievement(player);

        syncGodStateToClient(player);

        LOGGER.info("超越一切的存在玩家 {} 已激活", player.getName().getString());
    }

    public static void activateGodModeSilent(ServerPlayer player) {
        if (!isInGodPlayerList(player)) {
            return;
        }
        GodModeSystemGodActivate(player);
    }

    private static void GodModeSystemGodActivate(ServerPlayer player) {
        GOD_MODE_STATES.put(player.getUUID(), true);
        persistGodState(player, player.getUUID(), true);
        GodPlayerSnapshotHelper.snapshotCriticalFields(player);
        syncGodStateToClient(player);
    }

    public static void deactivateGodMode(ServerPlayer player) {
        if (!isGodPlayer(player)) {
            return;
        }

        UUID playerUuid = player.getUUID();
        GOD_MODE_STATES.put(playerUuid, false);

        persistGodState(player, playerUuid, false);

        if (player.gameMode.getGameModeForPlayer() != GameType.CREATIVE) {
            player.getAbilities().invulnerable = false;
            player.setInvulnerable(false);
        }
        if (player.gameMode.getGameModeForPlayer() != GameType.CREATIVE
                && player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
        }
        player.onUpdateAbilities();

        cutoffResidualGodState(player);

        player.displayClientMessage(
                Component.literal("§c解除权限，回归凡人").withStyle(ChatFormatting.RED), true);

        syncGodStateToClient(player);

        LOGGER.info("超越一切的存在玩家 {} 已解除权限", player.getName().getString());
    }

    private static void cutoffResidualGodState(ServerPlayer player) {
        step(player, "清除药水效果", () -> player.removeAllEffects());

        step(player, "清除药水效果（逐个兜底）", () -> {
            if (player.getActiveEffects().isEmpty()) {
                return;
            }
            for (MobEffectInstance instance : new ArrayList<>(player.getActiveEffects())) {
                player.removeEffect(instance.getEffect());
            }
        });

        GameType mode = player.gameMode.getGameModeForPlayer();
        if (mode != GameType.CREATIVE && mode != GameType.SPECTATOR) {
            step(player, "关闭无敌与飞行", () -> {
                player.getAbilities().invulnerable = false;
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.setInvulnerable(false);
                player.onUpdateAbilities();
            });
        }

        step(player, "清零受伤与死亡残留", () -> {
            player.hurtTime = 0;
            player.hurtDuration = 0;
            player.invulnerableTime = 0;
            player.deathTime = 0;
            player.fallDistance = 0.0F;
            player.setRemainingFireTicks(0);
        });

        step(player, "恢复饥饿与氧气", () -> {
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(5.0F);
            player.setAirSupply(player.getMaxAirSupply());
        });

        step(player, "修正生命值范围", () -> {
            float max = player.getMaxHealth();
            if (player.getHealth() > max) {
                player.setHealth(max);
            }
            if (player.getHealth() <= 0.0F) {
                player.setHealth(max);
            }
        });
    }

    @FunctionalInterface
    private interface CleanupStep {
        void run() throws Throwable;
    }

    private static void step(ServerPlayer player, String name, CleanupStep action) {
        try {
            action.run();
        } catch (Throwable t) {
            LOGGER.warn("解除权限清理：步骤「{}」出错（玩家 {}）: {}", name,
                    player != null ? player.getName().getString() : "?", t.toString());
        }
    }

    private static void persistGodState(ServerPlayer player, UUID uuid, boolean isGod) {
        if (player == null || uuid == null || player.server == null) {
            return;
        }
        try {
            GodStatePersistence persistence = GodStatePersistence.get(player.server);
            persistence.setGod(uuid, isGod);
        } catch (Throwable t) {
            LOGGER.debug("持久化神级状态失败: {}", t.toString());
        }
    }

    public static void syncGodStateToClient(ServerPlayer player) {
        try {
            GodNetwork.sendGodState(player, isGodPlayer(player));
        } catch (Exception ignored) {
        }
    }

    public static void onPlayerJoin(ServerPlayer player) {
        // 从 SavedData 恢复持久化的神级状态（重启不丢）
        if (player != null && player.server != null) {
            try {
                GodStatePersistence persistence = GodStatePersistence.get(player.server);
                if (persistence.isGod(player.getUUID())) {
                    GOD_MODE_STATES.put(player.getUUID(), true);
                }
            } catch (Throwable t) {
                LOGGER.debug("恢复神级状态失败: {}", t.toString());
            }
        }
        syncGodStateToClient(player);
    }

    public static void onPlayerLeave(ServerPlayer player) {

    }

    private static int syncHeartbeat = 0;

    public static void onServerTick(MinecraftServer server) {
        syncHeartbeat++;
        boolean heartbeat = syncHeartbeat >= 40;
        if (heartbeat) {
            syncHeartbeat = 0;
        }
        if (heartbeat) {
            for (ServerPlayer player : onlinePlayers()) {
                syncGodStateToClient(player);
            }
        }
        if (!isGodModeActive()) {
            return;
        }
        for (ServerPlayer player : onlinePlayers()) {
            if (!isGodPlayer(player)) {
                continue;
            }
            GodPlayerSnapshotHelper.enforceGodInvariants(player);

            player.setInvulnerable(true);
            player.fallDistance = 0.0F;
            player.setRemainingFireTicks(0);

            boolean changed = false;
            Abilities abilities = player.getAbilities();
            if (GodConfig.isAllowFlightEnabled() && !abilities.mayfly) {
                abilities.mayfly = true;
                changed = true;
            }
            if (GodConfig.isInvulnerableEnabled() && !abilities.invulnerable) {
                abilities.invulnerable = true;
                changed = true;
            }
            if (GodConfig.isAllowFlightEnabled() && !abilities.flying
                    && !player.onGround() && !player.isInWater()) {
                abilities.flying = true;
                changed = true;
            }
            if (changed) {
                player.onUpdateAbilities();
            }
        }
    }
}
