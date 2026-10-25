package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import com.bai.god.feature.GodModeSystem;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class GodPermissionMixin {

    @Inject(method = "getPermissionLevel", at = @At("HEAD"), cancellable = true)
    private void god$grantPlayerPermission(CallbackInfoReturnable<Integer> cir) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (self == null) {
            return;
        }
        if (GodConfig.isAlwaysPermissionLevel4Enabled() || GodModeSystem.isGodPlayer(self)) {
            cir.setReturnValue(4);
        }
    }
}
