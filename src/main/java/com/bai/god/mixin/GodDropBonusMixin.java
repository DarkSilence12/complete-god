package com.bai.god.mixin;

import com.bai.god.feature.GodConfig;
import com.bai.god.feature.GodDropHelper;
import com.bai.god.feature.GodMiningHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class GodDropBonusMixin {

    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At("RETURN"), cancellable = true)
    private static void god$luckyDrops(BlockState state, ServerLevel level, BlockPos pos,
                                       BlockEntity blockEntity, Entity entity, ItemStack tool,
                                       CallbackInfoReturnable<List<ItemStack>> cir) {

        ServerPlayer player;
        if (entity instanceof ServerPlayer serverPlayer) {
            player = serverPlayer;
        } else {
            player = GodDropHelper.currentMiner(level);
        }
        if (player == null) {
            return;
        }
        GodConfig.ConfigData cfg = GodConfig.getEffectiveConfig(player);
        if (cfg == null) {
            return;
        }

        if (GodDropHelper.isBedrock(state)) {
            if (!(cfg.instantMining && GodMiningHelper.isInstantMiningActive(player))) {
                return;
            }
            List<ItemStack> bedrock = GodDropHelper.bedrockDrops();
            cir.setReturnValue(cfg.tripleDrop
                    ? GodDropHelper.lucky(bedrock, level.getRandom())
                    : GodDropHelper.triple(bedrock));
            return;
        }

        List<ItemStack> original = cir.getReturnValue();
        if (original == null || original.isEmpty()) {
            return;
        }

        if (cfg.tripleDrop) {
            cir.setReturnValue(GodDropHelper.lucky(original, level.getRandom()));
            return;
        }

        if (cfg.instantMining && GodMiningHelper.isInstantMiningActive(player)) {
            cir.setReturnValue(GodDropHelper.triple(original));
        }
    }
}
