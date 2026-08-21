package net.silvertide.playerabilities_mortalboons.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.silvertide.player_abilities.api.AbilityAPI;
import net.silvertide.playerabilities_mortalboons.registry.ModAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockWaterRunMixin {
    @Unique
    private static final float MAX_LANDING_FALL_DISTANCE = 3.0f;

    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    private void playerabilities_mortalboons$waterRunningSurface(BlockState state, BlockGetter level, BlockPos pos,
                                                                 CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!cir.getReturnValue().isEmpty()) {
            return;
        }
        if (!(context instanceof EntityCollisionContext entityContext)
                || !(entityContext.getEntity() instanceof Player player)
                || !player.isSprinting()) {
            return;
        }
        if (!context.isAbove(Shapes.block(), pos, true)
                || player.getAbilities().flying
                || player.isFallFlying()
                || player.fallDistance > MAX_LANDING_FALL_DISTANCE) {
            return;
        }
        FluidState fluidState = state.getFluidState();
        if (!fluidState.isSource() || !fluidState.is(FluidTags.WATER)) {
            return;
        }
        if (AbilityAPI.getPassiveLevel(player, ModAbilities.WATER_RUNNING) == 0) {
            return;
        }
        if (level.getFluidState(pos.above()).is(FluidTags.WATER)) {
            return;
        }
        cir.setReturnValue(Shapes.block());
    }
}
