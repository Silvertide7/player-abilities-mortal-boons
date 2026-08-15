package net.silvertide.playerabilities_mortalboons.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.silvertide.player_abilities.api.AbilityAPI;
import net.silvertide.playerabilities_mortalboons.registry.ModAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClimbMixin {
    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    @Inject(method = "onClimbable", at = @At("RETURN"), cancellable = true)
    private void playerabilities_mortalboons$spiderClimbAnyWall(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !((Object) this instanceof Player player)) {
            return;
        }
        if (!player.horizontalCollision || player.isSpectator() || player.getAbilities().flying) {
            return;
        }
        if (AbilityAPI.getPassiveLevel(player, ModAbilities.SPIDER_CLIMB) > 0) {
            this.lastClimbablePos = Optional.of(player.blockPosition());
            cir.setReturnValue(true);
        }
    }
}
