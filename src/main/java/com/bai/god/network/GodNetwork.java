package com.bai.god.network;

import com.bai.god.God;
import com.bai.god.feature.GodModeSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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

/**
 * 完整神 —— Forge 网络层（移植自 Fabric 单通道 god:state）。
 * <p>收敛为一条 SimpleChannel（wanzhengshen:main），单一消息 {@link GodStateMessage}（S2C，携带神级状态布尔值）。
 * 版本谓词使用 {@link NetworkRegistry#acceptMissingOr}，同时接受协议版本 "1"、对端缺失（ABSENT）与原版连接（ALLOWVANILLA），
 * 从而允许「服务端装模组、客户端不装」也能正常进入服务器。</p>
 */
public final class GodNetwork {

    private GodNetwork() {
    }

    public static final ResourceLocation CHANNEL = new ResourceLocation("wanzhengshen", "main");

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
            CHANNEL,
            () -> PROTOCOL_VERSION,
            NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION),
            NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION)
    );

    static {
        CHANNEL_INSTANCE.messageBuilder(GodStateMessage.class, 0)
                .encoder(GodStateMessage::encode)
                .decoder(GodStateMessage::decode)
                .consumerMainThread(GodNetwork::handle)
                .add();
    }

    public static class GodStateMessage {

        public final boolean isGod;

        public GodStateMessage(boolean isGod) {
            this.isGod = isGod;
        }

        public static void encode(GodStateMessage msg, FriendlyByteBuf buf) {
            buf.writeBoolean(msg.isGod);
        }

        public static GodStateMessage decode(FriendlyByteBuf buf) {
            return new GodStateMessage(buf.readBoolean());
        }
    }

    public static void handle(GodStateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient()) {
                // 客户端分支：更新本地神级状态（GUI 开关的只读展示）
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        GodModeSystem.setClientGodState(mc.player.getUUID(), msg.isGod);
                    }
                });
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void register() {
        God.LOGGER.info("wanzhengshen 网络通道已就绪（{}）", CHANNEL);
    }

    /**
     * 仅当该玩家已安装本模组（连接注册了 wanzhengshen:main 通道）时才发送。
     * 未安装的客户端在握手时被放行进入，但不会收到任何神级状态包。
     */
    private static boolean hasGodChannel(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        ConnectionData data = NetworkHooks.getConnectionData(player.connection.connection);
        return data != null && data.getChannels().containsKey(CHANNEL);
    }

    public static void sendGodState(ServerPlayer player, boolean isGod) {
        if (player == null) {
            return;
        }
        if (!hasGodChannel(player)) {
            return;
        }
        try {
            CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    new GodStateMessage(isGod));
        } catch (Throwable t) {
            God.LOGGER.debug("同步神级状态失败: {}", t.toString());
        }
    }
}
