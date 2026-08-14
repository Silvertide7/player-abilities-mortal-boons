package net.silvertide.playerabilities_mortalboons;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.slf4j.Logger;

@Mod(PlayerAbilitiesMortalBoons.MODID)
public class PlayerAbilitiesMortalBoons {
    public static final String MODID = "playerabilities_mortalboons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public PlayerAbilitiesMortalBoons(IEventBus modBus) {
        modBus.addListener(PlayerAbilitiesMortalBoons::addPackFinders);
    }

    private static void addPackFinders(AddPackFindersEvent event) {
        event.addPackFinders(id("builtin_data_packs/default_boons"), PackType.SERVER_DATA,
                Component.literal("Mortal Boons Player Abilities Defaults"), PackSource.DEFAULT, false, Pack.Position.TOP);
    }
}
