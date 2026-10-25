package com.bai.god.mixin;

import com.bai.god.feature.GodModeSystem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class GodPlayerProtectionMixin {

    private static boolean god$isGod(Player self) {
        return GodModeSystem.isGodPlayer(self);
    }

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void god$onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if (reason == Entity.RemovalReason.CHANGED_DIMENSION) {
            return;
        }
        if (god$isGod((Player) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void god$onDeath(DamageSource source, CallbackInfo ci) {
        if (god$isGod((Player) (Object) this)) {
            ci.cancel();
        }
    }
}
