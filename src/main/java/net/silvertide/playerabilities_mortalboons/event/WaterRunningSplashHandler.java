package net.silvertide.playerabilities_mortalboons.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.silvertide.player_abilities.api.AbilityAPI;
import net.silvertide.playerabilities_mortalboons.PlayerAbilitiesMortalBoons;
import net.silvertide.playerabilities_mortalboons.registry.ModAbilities;

@EventBusSubscriber(modid = PlayerAbilitiesMortalBoons.MODID)
public final class WaterRunningSplashHandler {
    private static final int PARTICLE_INTERVAL_TICKS = 2;
    private static final int SOUND_INTERVAL_TICKS = 10;
    private static final float SPLASH_VOLUME = 0.12f;

    private WaterRunningSplashHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !player.isSprinting()
                || !player.onGround()
                || player.isInWater()) {
            return;
        }
        long gameTime = player.level().getGameTime();
        if (gameTime % PARTICLE_INTERVAL_TICKS != 0) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel) player.level();
        BlockPos below = BlockPos.containing(player.getX(), player.getY() - 0.1, player.getZ());
        FluidState fluidState = serverLevel.getFluidState(below);
        if (!fluidState.is(FluidTags.WATER) || !fluidState.isSource()) {
            return;
        }
        if (AbilityAPI.getPassiveLevel(player, ModAbilities.WATER_RUNNING) == 0) {
            return;
        }
        serverLevel.sendParticles(ParticleTypes.SPLASH,
                player.getX(), player.getY() + 0.1, player.getZ(), 4, 0.15, 0.05, 0.15, 0.1);
        if (gameTime % SOUND_INTERVAL_TICKS == 0) {
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_SWIM,
                    SoundSource.PLAYERS, SPLASH_VOLUME, 1.2f + serverLevel.random.nextFloat() * 0.4f);
        }
    }
}
