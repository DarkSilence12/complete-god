package com.bai.god.mixin;

import com.bai.god.feature.GodModeSystem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class GodEntityProtectionMixin {

    private static boolean god$isGod(Entity self) {
        return self instanceof Player player && GodModeSystem.isGodPlayer(player);
    }

    @Inject(method = "kill", at = @At("HEAD"), cancellable = true)
    private void god$onKill(CallbackInfo ci) {
        if (god$isGod((Entity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "discard", at = @At("HEAD"), cancellable = true)
    private void god$onDiscard(CallbackInfo ci) {
        if (god$isGod((Entity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void god$onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if (reason == Entity.RemovalReason.CHANGED_DIMENSION) {
            return;
        }
        if (god$isGod((Entity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "setRemoved", at = @At("HEAD"), cancellable = true)
    private void god$onSetRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        if (reason == Entity.RemovalReason.CHANGED_DIMENSION) {
            return;
        }
        if (god$isGod((Entity) (Object) this)) {
            ci.cancel();
        }
    }
}
