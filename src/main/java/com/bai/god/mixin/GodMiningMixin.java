package com.bai.god.mixin;

import com.bai.god.feature.GodMiningHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class GodMiningMixin {

    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    private void god$destroyProgress(Player player, BlockGetter level, BlockPos pos,
                                     CallbackInfoReturnable<Float> cir) {
        if (player == null) {
            return;
        }
        float original = cir.getReturnValueF();
        float modified = GodMiningHelper.applyMining(player, (BlockState) (Object) this, pos, original);
        if (modified != original) {
            cir.setReturnValue(modified);
        }
    }
}
