package com.bai.god.fabric;

import com.bai.god.God;
import com.bai.god.GodClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class CompleteGodFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(GodClient.createKeyMapping());
        ClientTickEvents.END_CLIENT_TICK.register(GodClient::onClientTick);
        FabricNetwork.registerClient();
        GodClient.init();
        God.LOGGER.info("完整神 Fabric 客户端入口初始化完成");
    }
}
