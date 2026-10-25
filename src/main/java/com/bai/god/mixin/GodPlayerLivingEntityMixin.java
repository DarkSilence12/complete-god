package com.bai.god.mixin;

import com.bai.god.feature.GodModeSystem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GodPlayerLivingEntityMixin {

    private static boolean god$isGod(LivingEntity self) {
        return self instanceof Player player && GodModeSystem.isGodPlayer(player);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void god$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (god$isGod((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void god$onSetHealth(float health, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!god$isGod(self)) {
            return;
        }

        if (health <= 0.0F || health < self.getHealth()) {
            ci.cancel();
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void god$onDeath(DamageSource source, CallbackInfo ci) {
        if (god$isGod((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "kill", at = @At("HEAD"), cancellable = true)
    private void god$onKill(CallbackInfo ci) {
        if (god$isGod((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }
}
