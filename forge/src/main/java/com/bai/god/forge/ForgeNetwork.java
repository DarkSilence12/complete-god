package com.bai.god.forge;

import com.bai.god.God;
import com.bai.god.GodClient;
import com.bai.god.network.GodNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public final class ForgeNetwork {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            GodNetwork.CHANNEL,
            () -> PROTOCOL_VERSION,
            NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION),
            NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION)
    );

    private ForgeNetwork() {
    }

    static {
        CHANNEL.messageBuilder(GodStateMessage.class, 0)
                .encoder(GodStateMessage::encode)
                .decoder(GodStateMessage::decode)
                .consumerMainThread(ForgeNetwork::handle)
                .add();
    }

    public static void register() {
        God.LOGGER.info("完整神 Forge 网络通道已就绪（{}）", GodNetwork.CHANNEL);
    }

    public static class GodStateMessage {

        public final boolean isGod;

        public GodStateMessage(boolean isGod) {
            this.isGod = isGod;
        }

        public static void encode(GodStateMessage msg, FriendlyByteBuf buf) {
            GodNetwork.writeState(buf, msg.isGod);
        }

        public static GodStateMessage decode(FriendlyByteBuf buf) {
            return new GodStateMessage(GodNetwork.readState(buf));
        }
    }

    private static void handle(GodStateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GodClient.onStatePacket(msg.isGod));
            }
        });
        ctx.setPacketHandled(true);
    }

    public static boolean canSendTo(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        ConnectionData data = NetworkHooks.getConnectionData(player.connection.connection);
        return data != null && data.getChannels().containsKey(GodNetwork.CHANNEL);
    }

    public static void sendGodState(ServerPlayer player, boolean isGod) {
        if (!canSendTo(player)) {
            return;
        }
        try {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new GodStateMessage(isGod));
        } catch (Throwable t) {
            God.LOGGER.debug("同步神级状态失败: {}", t.toString());
        }
    }
}
