package com.bai.god;

import com.bai.god.feature.GodModeSystem;
import com.bai.god.feature.GodPlayerJoinHandler;
import com.bai.god.feature.HiddenAchievementSystem;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class GodEvents {

    private GodEvents() {
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            GodModeSystem.initialize();
            HiddenAchievementSystem.initialize();
            GodPlayerJoinHandler.initialize();
        });
        ServerLifecycleEvents.SERVER_STARTED.register(GodModeSystem::setServer);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> GodModeSystem.clearServer());

        ServerTickEvents.END_SERVER_TICK.register(GodModeSystem::onServerTick);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            GodModeSystem.onPlayerJoin(player);
            GodPlayerJoinHandler.handlePlayerJoin(player);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                GodModeSystem.onPlayerLeave(handler.player));

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
                GodPlayerJoinHandler.handlePlayerClone(newPlayer, !alive));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
                GodPlayerJoinHandler.handlePlayerJoin(newPlayer));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("god")
                        .then(Commands.literal("on")
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayer();
                                    if (player == null) {
                                        ctx.getSource().sendFailure(
                                                Component.literal("该命令只能由玩家执行"));
                                        return 0;
                                    }
                                    GodModeSystem.activateGodMode(player);
                                    return 1;
                                }))
                        .then(Commands.literal("off")
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayer();
                                    if (player == null) {
                                        ctx.getSource().sendFailure(
                                                Component.literal("该命令只能由玩家执行"));
                                        return 0;
                                    }
                                    GodModeSystem.deactivateGodMode(player);
                                    return 1;
                                }))));

        God.LOGGER.info("god 服务端事件与 /god 指令已接线");
    }
}
