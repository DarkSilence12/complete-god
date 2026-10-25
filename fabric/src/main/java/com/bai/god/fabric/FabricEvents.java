package com.bai.god.fabric;

import com.bai.god.GodEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public final class FabricEvents {

    private FabricEvents() {
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> GodEvents.onServerStarting());
        ServerLifecycleEvents.SERVER_STARTED.register(GodEvents::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> GodEvents.onServerStopping());

        ServerTickEvents.END_SERVER_TICK.register(GodEvents::onServerTick);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                GodEvents.onPlayerJoin(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                GodEvents.onPlayerLeave(handler.player));

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
                GodEvents.onPlayerClone(newPlayer, !alive));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
                GodEvents.onPlayerRespawn(newPlayer));
    }
}
