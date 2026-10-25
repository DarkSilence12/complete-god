package com.bai.god.network;

import com.bai.god.God;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class GodNetwork {

    private GodNetwork() {
    }

    public static final ResourceLocation CHANNEL = new ResourceLocation("god", "state");

    public static void register() {
        God.LOGGER.info("god 网络通道已就绪（{}）", CHANNEL);
    }

    public static void sendGodState(ServerPlayer player, boolean isGod) {
        if (player == null) {
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        try {
            buf.writeBoolean(isGod);
            ServerPlayNetworking.send(player, CHANNEL, buf);
        } catch (Throwable t) {

            God.LOGGER.debug("同步神级状态失败: {}", t.toString());
        }
    }
}
