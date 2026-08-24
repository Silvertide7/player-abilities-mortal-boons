package net.silvertide.playerabilities_mortalboons.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.silvertide.player_abilities.api.AbilityAPI;
import net.silvertide.playerabilities_mortalboons.PlayerAbilitiesMortalBoons;
import net.silvertide.playerabilities_mortalboons.registry.ModAbilities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = PlayerAbilitiesMortalBoons.MODID)
public final class SlipstreamHandler {
    private static final Map<Projectile, Set<UUID>> SLIPPED_PAST =
            Collections.synchronizedMap(new WeakHashMap<>());

    private SlipstreamHandler() {
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof AbstractArrow arrow)
                || !(event.getRayTraceResult() instanceof EntityHitResult entityHit)
                || !(entityHit.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        Set<UUID> slippedPast = SLIPPED_PAST.get(arrow);
        if (slippedPast != null && slippedPast.contains(player.getUUID())) {
            event.setCanceled(true);
            return;
        }
        if (!player.isSprinting()) {
            return;
        }
        int level = AbilityAPI.getPassiveLevel(player, ModAbilities.SLIPSTREAM);
        if (level == 0 || player.level().random.nextFloat() >= ModAbilities.SLIPSTREAM.missChance(level)) {
            return;
        }
        SLIPPED_PAST.computeIfAbsent(arrow, key -> new HashSet<>()).add(player.getUUID());
        player.level().playSound(null, player.blockPosition(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.6f, 1.5f);
        event.setCanceled(true);
    }
}
