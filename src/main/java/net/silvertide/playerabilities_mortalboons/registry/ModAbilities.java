package net.silvertide.playerabilities_mortalboons.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.silvertide.player_abilities.api.AbilityRegistry;
import net.silvertide.playerabilities_mortalboons.PlayerAbilitiesMortalBoons;
import net.silvertide.playerabilities_mortalboons.ability.BloodscentAbility;
import net.silvertide.playerabilities_mortalboons.ability.GuardianAngelAbility;
import net.silvertide.playerabilities_mortalboons.ability.SecondWindAbility;
import net.silvertide.playerabilities_mortalboons.ability.SpiderClimbAbility;
import net.silvertide.playerabilities_mortalboons.ability.SwiftStepAbility;

@EventBusSubscriber(modid = PlayerAbilitiesMortalBoons.MODID)
public final class ModAbilities {
    public static final GuardianAngelAbility GUARDIAN_ANGEL = new GuardianAngelAbility();
    public static final SecondWindAbility SECOND_WIND = new SecondWindAbility();
    public static final BloodscentAbility BLOODSCENT = new BloodscentAbility();
    public static final SwiftStepAbility SWIFT_STEP = new SwiftStepAbility();
    public static final SpiderClimbAbility SPIDER_CLIMB = new SpiderClimbAbility();

    private ModAbilities() {
    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(AbilityRegistry.ABILITY_REGISTRY_KEY, helper -> {
            helper.register(PlayerAbilitiesMortalBoons.id("guardian_angel"), GUARDIAN_ANGEL);
            helper.register(PlayerAbilitiesMortalBoons.id("second_wind"), SECOND_WIND);
            helper.register(PlayerAbilitiesMortalBoons.id("bloodscent"), BLOODSCENT);
            helper.register(PlayerAbilitiesMortalBoons.id("swift_step"), SWIFT_STEP);
            helper.register(PlayerAbilitiesMortalBoons.id("spider_climb"), SPIDER_CLIMB);
        });
    }
}
