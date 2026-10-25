package com.bai.god.platform;

import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public interface Platform {

    Path configDir();

    Path gameDir();

    boolean isModLoaded(String modId);

    boolean isClient();

    boolean canSendTo(ServerPlayer player);

    void sendGodState(ServerPlayer player, boolean isGod);
}
