package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GodPlayerPassiveMixin {

    private static final int GOD$HEAL_INTERVAL = 80;
    private static final float GOD$HEAL_RATIO = 0.35F;

    @Inject(method = "tick", at = @At("HEAD"))
    private void god$autoHeal(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) {
            return;
        }
        if (!GodConfig.getEffectiveConfig(player).autoHeal) {
            return;
        }
        if (self.tickCount % GOD$HEAL_INTERVAL != 0) {
            return;
        }
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(player.getMaxHealth() * GOD$HEAL_RATIO);
        }
    }
}
