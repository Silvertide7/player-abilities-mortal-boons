package net.silvertide.playerabilities_mortalboons.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.silvertide.player_abilities.api.AbilityRegistry;
import net.silvertide.playerabilities_mortalboons.PlayerAbilitiesMortalBoons;
import net.silvertide.playerabilities_mortalboons.ability.BloodscentAbility;
import net.silvertide.playerabilities_mortalboons.ability.GuardianAngelAbility;
import net.silvertide.playerabilities_mortalboons.ability.SecondWindAbility;
import net.silvertide.playerabilities_mortalboons.ability.SwiftStepAbility;

@EventBusSubscriber(modid = PlayerAbilitiesMortalBoons.MODID)
public final class ModAbilities {
    private ModAbilities() {
    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(AbilityRegistry.ABILITY_REGISTRY_KEY, helper -> {
            helper.register(PlayerAbilitiesMortalBoons.id("guardian_angel"), new GuardianAngelAbility());
            helper.register(PlayerAbilitiesMortalBoons.id("second_wind"), new SecondWindAbility());
            helper.register(PlayerAbilitiesMortalBoons.id("bloodscent"), new BloodscentAbility());
            helper.register(PlayerAbilitiesMortalBoons.id("swift_step"), new SwiftStepAbility());
        });
    }
}
