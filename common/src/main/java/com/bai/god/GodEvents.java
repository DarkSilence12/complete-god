package com.bai.god;

import com.bai.god.feature.GodModeSystem;
import com.bai.god.feature.GodPlayerJoinHandler;
import com.bai.god.feature.HiddenAchievementSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class GodEvents {

    private GodEvents() {
    }

    public static void onServerStarting() {
        GodModeSystem.initialize();
        HiddenAchievementSystem.initialize();
        GodPlayerJoinHandler.initialize();
    }

    public static void onServerStarted(MinecraftServer server) {
        GodModeSystem.setServer(server);
    }

    public static void onServerStopping() {
        GodModeSystem.clearServer();
    }

    public static void onServerTick(MinecraftServer server) {
        GodModeSystem.onServerTick(server);
    }

    public static void onPlayerJoin(ServerPlayer player) {
        GodModeSystem.onPlayerJoin(player);
        GodPlayerJoinHandler.handlePlayerJoin(player);
    }

    public static void onPlayerLeave(ServerPlayer player) {
        GodModeSystem.onPlayerLeave(player);
    }

    public static void onPlayerClone(ServerPlayer player, boolean wasDeath) {
        GodPlayerJoinHandler.handlePlayerClone(player, wasDeath);
    }

    public static void onPlayerRespawn(ServerPlayer player) {
        GodPlayerJoinHandler.handlePlayerJoin(player);
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandNode<CommandSourceStack> on = Commands.literal("on")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) {
                        ctx.getSource().sendFailure(
                                Component.literal("该命令只能由玩家执行"));
                        return 0;
                    }
                    GodModeSystem.activateGodMode(player);
                    return 1;
                }).build();
        CommandNode<CommandSourceStack> off = Commands.literal("off")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) {
                        ctx.getSource().sendFailure(
                                Component.literal("该命令只能由玩家执行"));
                        return 0;
                    }
                    GodModeSystem.deactivateGodMode(player);
                    return 1;
                }).build();

        CommandNode<CommandSourceStack> godRoot = dispatcher.getRoot().getChild("god");
        if (godRoot != null) {
            godRoot.addChild(on);
            godRoot.addChild(off);
            God.LOGGER.info("完整神 /god on|off 已并入现有 /god 命令树");
            return;
        }
        dispatcher.register(Commands.literal("god").then(on).then(off));
        God.LOGGER.info("完整神 服务端事件与 /god 指令已接线");
    }
}
