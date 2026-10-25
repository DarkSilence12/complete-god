package com.bai.god;

import com.bai.god.client.render.GodConfigScreen;
import com.bai.god.feature.GodConfig;
import com.bai.god.feature.GodModeSystem;
import com.bai.god.network.GodNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class GodClient implements ClientModInitializer {

    public static final String KEY_CATEGORY = "category.god.config";
    public static final String KEY_OPEN_CONFIG = "key.god.config_open";

    public static KeyMapping openConfigKey;

    @Override
    public void onInitializeClient() {
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_OPEN_CONFIG,
                com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                KEY_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.player == null || openConfigKey == null) {
                return;
            }
            while (openConfigKey.consumeClick()) {
                mc.setScreen(new GodConfigScreen(mc.screen));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(GodNetwork.CHANNEL,
                (client, handler, buf, responseSender) -> {
                    boolean isGod = buf.readBoolean();
                    client.execute(() -> {
                        if (client.player != null) {
                            GodModeSystem.setClientGodState(client.player.getUUID(), isGod);
                        }
                    });
                });

        GodConfig.load();

        God.LOGGER.info("「-1 配置」按键已注册（默认为 = 键）");
    }
}
