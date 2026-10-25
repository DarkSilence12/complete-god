package com.bai.god;

import com.bai.god.feature.GodModeSystem;
import com.bai.god.feature.GodPlayerJoinHandler;
import com.bai.god.feature.HiddenAchievementSystem;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.stream.Collectors;

/**
 * 完整神 —— 服务端事件与 /god 指令接线（Forge 版）。
 * <p>/god on|off 与「言出法随」的 /god shen 共存于同一棵命令树：
 * 完整神最后注册（LOWEST 优先级），检测到已存在的 /god 根节点时向其上追加 on/off 子命令而非整体覆盖。</p>
 */
public final class GodEvents {

    private GodEvents() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(GodEvents.class);
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        GodModeSystem.initialize();
        HiddenAchievementSystem.initialize();
        GodPlayerJoinHandler.initialize();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        GodModeSystem.setServer(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        GodModeSystem.clearServer();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GodModeSystem.onServerTick(ServerLifecycleHooks.getCurrentServer());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            GodModeSystem.onPlayerJoin(player);
            GodPlayerJoinHandler.handlePlayerJoin(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            GodModeSystem.onPlayerLeave(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer) {
            GodPlayerJoinHandler.handlePlayerClone(newPlayer, event.isWasDeath());
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer) {
            GodPlayerJoinHandler.handlePlayerJoin(newPlayer);
        }
    }

    /**
     * 注册 /god on|off（与原版 Fabric 完整神命令一致）。
     * LOWEST 优先级保证最后执行：若 /god 根节点已被「言出法随」（/god shen）占用，
     * 则向已有节点追加 on/off 子命令（Brigadier 同名根节点整体覆盖会互相顶掉，追加则共存）。
     * 单独安装完整神时 /god 根节点不存在，正常注册完整命令树。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegisterCommands(RegisterCommandsEvent event) {
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

        CommandNode<CommandSourceStack> godRoot = event.getDispatcher().getRoot().getChild("god");
        if (godRoot != null) {
            // 与「言出法随」/god shen 共存：向已有 /god 根节点追加 on/off
            godRoot.addChild(on);
            godRoot.addChild(off);
        } else {
            event.getDispatcher().register(Commands.literal("god").then(on).then(off));
        }

        String children = godRoot != null
                ? godRoot.getChildren().stream().map(CommandNode::getName).collect(Collectors.joining(","))
                : "on,off";
        God.LOGGER.info("完整神 /god 命令已接线（当前 /god 子命令: {}）", children);
    }
}
