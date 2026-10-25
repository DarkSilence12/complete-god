package com.bai.god;

import com.bai.god.client.render.GodConfigScreen;
import com.bai.god.feature.GodConfig;
import com.bai.god.feature.GodModeSystem;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class GodClient {

    public static final String KEY_CATEGORY = "category.complete_god.config";
    public static final String KEY_OPEN_CONFIG = "key.complete_god.config_open";

    public static KeyMapping openConfigKey;

    private GodClient() {
    }

    public static KeyMapping createKeyMapping() {
        openConfigKey = new KeyMapping(
                KEY_OPEN_CONFIG,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                KEY_CATEGORY);
        return openConfigKey;
    }

    public static void onClientTick(Minecraft mc) {
        if (mc.player == null || openConfigKey == null) {
            return;
        }
        while (openConfigKey.consumeClick()) {
            mc.setScreen(new GodConfigScreen(mc.screen));
        }
    }

    public static void onStatePacket(boolean isGod) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            GodModeSystem.setClientGodState(mc.player.getUUID(), isGod);
        }
    }

    public static void init() {
        GodConfig.load();
        God.LOGGER.info("「-1 配置」按键已注册（默认为 = 键）");
    }
}
