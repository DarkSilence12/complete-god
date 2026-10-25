package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GodDamageReductionMixin {

    private static final float REDUCTION = 0.32F;

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void god$damageReduction(DamageSource source, float amount,
                                     CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) {
            return;
        }
        if (!GodConfig.getEffectiveConfig(player).damageReduction) {
            return;
        }
        float after = cir.getReturnValueF();
        if (after <= 0.0F) {
            return;
        }
        cir.setReturnValue(after * (1.0F - REDUCTION));
    }
}
