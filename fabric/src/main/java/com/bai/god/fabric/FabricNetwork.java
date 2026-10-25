package com.bai.god.fabric;

import com.bai.god.God;
import com.bai.god.GodClient;
import com.bai.god.network.GodNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public final class FabricNetwork {

    private FabricNetwork() {
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(GodNetwork.CHANNEL,
                (client, handler, buf, responseSender) -> {
                    boolean isGod = GodNetwork.readState(buf);
                    client.execute(() -> GodClient.onStatePacket(isGod));
                });
    }

    public static void sendGodState(ServerPlayer player, boolean isGod) {
        if (player == null) {
            return;
        }
        if (!ServerPlayNetworking.canSend(player, GodNetwork.CHANNEL)) {
            return;
        }
        try {
            FriendlyByteBuf buf = PacketByteBufs.create();
            GodNetwork.writeState(buf, isGod);
            ServerPlayNetworking.send(player, GodNetwork.CHANNEL, buf);
        } catch (Throwable t) {
            God.LOGGER.debug("同步神级状态失败: {}", t.toString());
        }
    }
}
