package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import com.bai.god.feature.GodModeSystem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class GodHungerMixin {

    private static final float GOD$MIN_SATURATION = 5.0F;
    private static final int GOD$MAX_FOOD = 20;

    @Inject(method = "tick", at = @At("HEAD"))
    private void god$keepFed(Player player, CallbackInfo ci) {
        if (player == null
                || !GodModeSystem.isGodPlayer(player)
                || !GodConfig.isAntiHungerEnabled()) {
            return;
        }
        FoodData self = (FoodData) (Object) this;
        if (self.getFoodLevel() != GOD$MAX_FOOD) {
            self.setFoodLevel(GOD$MAX_FOOD);
        }
        self.setSaturation(GOD$MIN_SATURATION);
    }
}
