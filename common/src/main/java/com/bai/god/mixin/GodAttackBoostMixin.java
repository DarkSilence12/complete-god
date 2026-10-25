package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GodAttackBoostMixin {

    private static final float ATTACK_MULTIPLIER = 1.2F;

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void god$attackBoost(DamageSource source, float amount,
                                 CallbackInfoReturnable<Float> cir) {
        float current = cir.getReturnValueF();
        if (current <= 0.0F) {
            return;
        }
        Player attacker = god$resolveAttacker(source);
        if (attacker == null) {
            return;
        }
        if (!GodConfig.getEffectiveConfig(attacker).attackBoost) {
            return;
        }
        cir.setReturnValue(current * ATTACK_MULTIPLIER);
    }

    private static Player god$resolveAttacker(DamageSource source) {
        if (source == null) {
            return null;
        }
        Player player = god$asPlayer(source.getEntity());
        if (player != null) {
            return player;
        }
        player = god$asPlayer(source.getDirectEntity());
        if (player != null) {
            return player;
        }
        player = god$ownerAsPlayer(source.getEntity());
        if (player != null) {
            return player;
        }
        return god$ownerAsPlayer(source.getDirectEntity());
    }

    private static Player god$asPlayer(Entity entity) {
        return entity instanceof Player player ? player : null;
    }

    private static Player god$ownerAsPlayer(Entity entity) {
        if (entity instanceof OwnableEntity ownable) {
            try {
                if (ownable.getOwner() instanceof Player player) {
                    return player;
                }
            } catch (Throwable ignored) {
            }
        }
        if (entity instanceof Projectile projectile) {
            try {
                if (projectile.getOwner() instanceof Player player) {
                    return player;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }
}
