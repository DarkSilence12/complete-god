package com.bai.god.fabric;

import com.bai.god.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public class FabricPlatform implements Platform {

    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Path gameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean canSendTo(ServerPlayer player) {
        return player != null && ServerPlayNetworking.canSend(player, com.bai.god.network.GodNetwork.CHANNEL);
    }

    @Override
    public void sendGodState(ServerPlayer player, boolean isGod) {
        FabricNetwork.sendGodState(player, isGod);
    }
}
