package net.silvertide.playerabilities_mortalboons.ability;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.silvertide.player_abilities.api.AbilityTrigger;
import net.silvertide.player_abilities.api.EffectGrant;
import net.silvertide.player_abilities.api.PlayerTriggers;
import net.silvertide.player_abilities.api.TriggeredAbility;

import java.util.List;

public final class BloodscentAbility extends TriggeredAbility<LivingEntity> {
    private static final int MAX_LEVEL = 3;
    private static final int COOLDOWN_TICKS = 600;
    private static final int KILLS_TO_RECHARGE = 2;

    @Override
    public AbilityTrigger<LivingEntity> getTrigger() {
        return PlayerTriggers.KILL;
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
    public int getKillRequirement(int level) {
        return KILLS_TO_RECHARGE;
    }

    @Override
    public List<EffectGrant> getEffectGrants(int level) {
        int durationTicks = byLevel(level, 160, 200, 240);
        return List.of(
                new EffectGrant(MobEffects.DAMAGE_BOOST, durationTicks, byLevel(level, 0, 0, 1)),
                new EffectGrant(MobEffects.MOVEMENT_SPEED, durationTicks, 0));
    }
}
