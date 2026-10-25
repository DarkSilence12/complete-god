package com.bai.god.feature;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HiddenAchievementSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger("god-hidden-achievement");

    private static final ConcurrentHashMap<UUID, Set<String>> PLAYER_ACHIEVEMENTS = new ConcurrentHashMap<>();

    public static final String BEYOND_EVERYTHING_ACHIEVEMENT = "beyond_everything";

    public static void giveHiddenAchievement(ServerPlayer player, String achievementId, String displayName) {
        UUID playerId = player.getUUID();
        PLAYER_ACHIEVEMENTS.computeIfAbsent(playerId, k -> new HashSet<>()).add(achievementId);
        player.displayClientMessage(
                Component.literal("§6§l隐藏成就解锁: §e" + displayName)
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
    }

    public static boolean hasHiddenAchievement(ServerPlayer player, String achievementId) {
        Set<String> achievements = PLAYER_ACHIEVEMENTS.get(player.getUUID());
        return achievements != null && achievements.contains(achievementId);
    }

    public static void giveBeyondEverythingAchievement(ServerPlayer player) {
        if (!GodModeSystem.isGodPlayer(player)) {
            return;
        }
        if (hasHiddenAchievement(player, BEYOND_EVERYTHING_ACHIEVEMENT)) {
            return;
        }
        giveHiddenAchievement(player, BEYOND_EVERYTHING_ACHIEVEMENT,
                "你已超越一切规则，获得至高无上的力量");
        player.displayClientMessage(
                Component.literal("§a§l此成就不会在成就页面显示，只有你知道它的存在")
                        .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), false);
    }

    public static void initialize() {
        LOGGER.info("隐藏成就系统已初始化");
    }
}
