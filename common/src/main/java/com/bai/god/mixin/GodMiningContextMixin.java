package com.bai.god.mixin;

import com.bai.god.feature.GodDropHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class GodMiningContextMixin {

    @Inject(method = "playerDestroy", at = @At("HEAD"))
    private void god$noteMiner(Level level, Player player, BlockPos pos, BlockState state,
                               BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel) {
            GodDropHelper.noteMiner(serverPlayer);
        }
    }
}
