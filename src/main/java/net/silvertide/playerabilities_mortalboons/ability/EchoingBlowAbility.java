package net.silvertide.playerabilities_mortalboons.ability;

import net.silvertide.player_abilities.api.PassiveAbility;

public final class EchoingBlowAbility extends PassiveAbility {
    private static final int MAX_LEVEL = 3;

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public float echoChance(int level) {
        return byLevel(level, 0.25f, 0.4f, 0.6f);
    }

    public int echoAttempts(int level) {
        return byLevel(level, 1, 1, 2);
    }
}
