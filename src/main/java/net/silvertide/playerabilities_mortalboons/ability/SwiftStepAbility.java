package net.silvertide.playerabilities_mortalboons.ability;

import net.minecraft.world.effect.MobEffects;
import net.silvertide.player_abilities.api.AbilityTrigger;
import net.silvertide.player_abilities.api.EffectGrant;
import net.silvertide.player_abilities.api.PlayerTriggers;
import net.silvertide.player_abilities.api.TriggeredAbility;

import java.util.List;

public final class SwiftStepAbility extends TriggeredAbility<PlayerTriggers.DamageTaken> {
    private static final int MAX_LEVEL = 3;
    private static final int COOLDOWN_TICKS = 1800;
    private static final float DAMAGE_TO_RECHARGE = 10.0f;

    @Override
    public AbilityTrigger<PlayerTriggers.DamageTaken> getTrigger() {
        return PlayerTriggers.DAMAGE_TAKEN;
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
    public float getDamageTakenRequirement(int level) {
        return DAMAGE_TO_RECHARGE;
    }

    @Override
    public List<EffectGrant> getEffectGrants(int level) {
        return List.of(new EffectGrant(MobEffects.MOVEMENT_SPEED,
                byLevel(level, 200, 300, 300), byLevel(level, 0, 0, 1)));
    }
}
