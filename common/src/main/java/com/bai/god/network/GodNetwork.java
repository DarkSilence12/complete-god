package com.bai.god.network;

import com.bai.god.God;
import com.bai.god.platform.Platforms;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class GodNetwork {

    public static final ResourceLocation CHANNEL = new ResourceLocation(God.MOD_ID, "state");

    private GodNetwork() {
    }

    public static void register() {
        God.LOGGER.info("完整神 网络通道已就绪（{}）", CHANNEL);
    }

    public static void writeState(FriendlyByteBuf buf, boolean isGod) {
        buf.writeBoolean(isGod);
    }

    public static boolean readState(FriendlyByteBuf buf) {
        return buf.readBoolean();
    }

    public static void sendGodState(ServerPlayer player, boolean isGod) {
        if (player == null) {
            return;
        }
        try {
            Platforms.get().sendGodState(player, isGod);
        } catch (Throwable t) {
            God.LOGGER.debug("同步神级状态失败: {}", t.toString());
        }
    }
}
