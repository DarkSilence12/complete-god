package com.bai.god;

import com.bai.god.client.render.GodConfigScreen;
import com.bai.god.feature.GodConfig;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

/**
 * 完整神 —— Forge 客户端入口（Fabric 版移植）。
 * <p>「-1 配置」按键默认 = 键；神级状态包由 {@link com.bai.god.network.GodNetwork} 的客户端分支接收。</p>
 */
@Mod.EventBusSubscriber(modid = God.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GodClient {

    public static final String KEY_CATEGORY = "category.wanzhengshen.config";
    public static final String KEY_OPEN_CONFIG = "key.wanzhengshen.config_open";

    public static KeyMapping openConfigKey;

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        openConfigKey = new KeyMapping(
                KEY_OPEN_CONFIG,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                KEY_CATEGORY);
        event.register(openConfigKey);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        GodConfig.load();
        MinecraftForge.EVENT_BUS.register(ClientTickHandler.class);
        God.LOGGER.info("「-1 配置」按键已注册（默认为 = 键）");
    }

    /** 客户端 tick：检测按键打开配置界面（Forge 事件总线）。 */
    @Mod.EventBusSubscriber(modid = God.MOD_ID, value = Dist.CLIENT)
    public static class ClientTickHandler {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || openConfigKey == null) {
                return;
            }
            while (openConfigKey.consumeClick()) {
                mc.setScreen(new GodConfigScreen(mc.screen));
            }
        }
    }
}
