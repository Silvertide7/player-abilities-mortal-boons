package net.silvertide.playerabilities_mortalboons;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PlayerAbilitiesMortalBoons.MODID)
public class PlayerAbilitiesMortalBoons {
    public static final String MODID = "playerabilities_mortalboons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PlayerAbilitiesMortalBoons(IEventBus modBus, ModContainer container) {
    }
}
