package net.silvertide.playerabilities_mortalboons.ability;

import net.silvertide.player_abilities.api.PassiveAbility;

public final class SlipstreamAbility extends PassiveAbility {
    private static final int MAX_LEVEL = 3;

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public float missChance(int level) {
        return byLevel(level, 0.25f, 0.4f, 0.6f);
    }
}
