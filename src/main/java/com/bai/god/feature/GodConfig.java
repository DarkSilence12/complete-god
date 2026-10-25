package com.bai.god.feature;

import com.bai.god.God;
import com.bai.god.util.GodEnv;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class GodConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final int FLAG_COUNT = 12;

    private static File configFile;

    private static ConfigData data = new ConfigData();

    private static File configFile() {
        if (configFile == null) {
            configFile = GodEnv.configFile("god_config.json");
        }
        return configFile;
    }

    public static boolean isInstantMiningEnabled() { return data.instantMining; }

    public static void setInstantMiningEnabled(boolean v) { data.instantMining = v; save(); }

    public static boolean isAllowFlightEnabled() { return data.allowFlight; }

    public static void setAllowFlightEnabled(boolean v) { data.allowFlight = v; save(); }

    public static boolean isInvulnerableEnabled() { return data.invulnerable; }

    public static void setInvulnerableEnabled(boolean v) { data.invulnerable = v; save(); }

    public static boolean isAntiHungerEnabled() { return data.antiHunger; }

    public static void setAntiHungerEnabled(boolean v) { data.antiHunger = v; save(); }

    public static boolean isAntiNegativeEffectsEnabled() { return data.antiNegativeEffects; }

    public static void setAntiNegativeEffectsEnabled(boolean v) { data.antiNegativeEffects = v; save(); }

    public static boolean isDamageReductionEnabled() { return data.damageReduction; }

    public static void setDamageReductionEnabled(boolean v) { data.damageReduction = v; save(); }

    public static boolean isAttackBoostEnabled() { return data.attackBoost; }

    public static void setAttackBoostEnabled(boolean v) { data.attackBoost = v; save(); }

    public static boolean isAutoHealEnabled() { return data.autoHeal; }

    public static void setAutoHealEnabled(boolean v) { data.autoHeal = v; save(); }

    public static boolean isMiningSpeedBoostEnabled() { return data.miningSpeedBoost; }

    public static void setMiningSpeedBoostEnabled(boolean v) { data.miningSpeedBoost = v; save(); }

    public static boolean isTripleDropEnabled() { return data.tripleDrop; }

    public static void setTripleDropEnabled(boolean v) { data.tripleDrop = v; save(); }

    public static boolean isAlwaysPermissionLevel4Enabled() { return data.alwaysPermissionLevel4; }

    public static void setAlwaysPermissionLevel4Enabled(boolean v) { data.alwaysPermissionLevel4 = v; save(); }

    public static boolean isGodModeAutoOnJoinEnabled() { return data.godModeAutoOnJoin; }

    public static void setGodModeAutoOnJoinEnabled(boolean v) { data.godModeAutoOnJoin = v; save(); }

    public static ConfigData getEffectiveConfig(Player player) {
        return data;
    }

    public static ConfigData getGlobalConfig() {
        return data;
    }

    public static void load() {
        try {
            File file = configFile();
            if (file.exists()) {
                try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                    data = GSON.fromJson(reader, ConfigData.class);
                }
            }
        } catch (Exception e) {
            God.LOGGER.error("读取 god_config.json 失败", e);
        }
        if (data == null) {
            data = new ConfigData();
        }
    }

    public static void save() {
        try {
            File file = configFile();
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
            God.LOGGER.error("写入 god_config.json 失败", e);
        }
    }

    public static class ConfigData {

        public boolean instantMining = true;

        public boolean allowFlight = true;

        public boolean invulnerable = true;

        public boolean antiHunger = true;

        public boolean antiNegativeEffects = true;

        public boolean damageReduction = false;

        public boolean attackBoost = false;

        public boolean autoHeal = false;

        public boolean miningSpeedBoost = false;

        public boolean tripleDrop = false;

        public boolean alwaysPermissionLevel4 = true;

        public boolean godModeAutoOnJoin = false;
    }
}
