package net.silvertide.playerabilities_mortalboons.ability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.silvertide.player_abilities.api.AbilityTrigger;
import net.silvertide.player_abilities.api.EffectGrant;
import net.silvertide.player_abilities.api.PlayerTriggers;
import net.silvertide.player_abilities.api.TriggeredAbility;

import java.util.List;

public final class SecondWindAbility extends TriggeredAbility<PlayerTriggers.HealthChange> {
    private static final int MAX_LEVEL = 3;
    private static final int COOLDOWN_TICKS = 18000;
    private static final int EFFECT_DURATION_TICKS = 600;
    private static final float TRIGGER_HEALTH_FRACTION = 0.3f;

    @Override
    public AbilityTrigger<PlayerTriggers.HealthChange> getTrigger() {
        return PlayerTriggers.HEALTH_DROPPED;
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    @Override
    public int getCooldownTicks(int level) {
        return COOLDOWN_TICKS;
    }

    @Override
    public boolean shouldTrigger(ServerPlayer player, int level, PlayerTriggers.HealthChange context) {
        return context.droppedBelow(TRIGGER_HEALTH_FRACTION);
    }

    @Override
    public List<EffectGrant> getEffectGrants(int level) {
        return List.of(new EffectGrant(MobEffects.REGENERATION, EFFECT_DURATION_TICKS, byLevel(level, 0, 1, 2)));
    }
}
