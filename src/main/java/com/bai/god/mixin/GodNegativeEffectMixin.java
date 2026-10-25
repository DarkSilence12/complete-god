package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import com.bai.god.feature.GodModeSystem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GodNegativeEffectMixin {

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
            at = @At("HEAD"), cancellable = true)
    private void god$blockNegativeEffect(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (god$shouldBlockEffect(effect)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"), cancellable = true)
    private void god$blockNegativeEffectWithSource(MobEffectInstance effect, Entity source,
                                                   CallbackInfoReturnable<Boolean> cir) {
        if (god$shouldBlockEffect(effect)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "forceAddEffect", at = @At("HEAD"), cancellable = true)
    private void god$blockForcedNegativeEffect(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        if (god$shouldBlockEffect(effect)) {
            ci.cancel();
        }
    }

    private boolean god$shouldBlockEffect(MobEffectInstance effect) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) {
            return false;
        }
        if (effect == null || effect.getEffect() == null) {
            return false;
        }
        if (!GodModeSystem.isGodPlayer(player)) {
            return false;
        }
        if (!GodConfig.getEffectiveConfig(player).antiNegativeEffects) {
            return false;
        }
        return !effect.getEffect().isBeneficial();
    }
}
