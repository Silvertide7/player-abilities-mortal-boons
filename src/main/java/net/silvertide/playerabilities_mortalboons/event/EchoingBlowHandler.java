package net.silvertide.playerabilities_mortalboons.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.silvertide.player_abilities.api.AbilityAPI;
import net.silvertide.playerabilities_mortalboons.PlayerAbilitiesMortalBoons;
import net.silvertide.playerabilities_mortalboons.registry.ModAbilities;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = PlayerAbilitiesMortalBoons.MODID)
public final class EchoingBlowHandler {
    private EchoingBlowHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)
                || !(event.getPlayer() instanceof ServerPlayer player)
                || player.isCreative()) {
            return;
        }
        int level = AbilityAPI.getPassiveLevel(player, ModAbilities.ECHOING_BLOW);
        if (level == 0 || !player.hasCorrectToolForDrops(event.getState())) {
            return;
        }
        if (serverLevel.random.nextFloat() >= ModAbilities.ECHOING_BLOW.echoChance(level)) {
            return;
        }
        List<BlockPos> matches = matchingNeighbors(serverLevel, player, event.getPos(), event.getState().getBlock());
        if (!matches.isEmpty()) {
            echoBreak(serverLevel, matches.get(serverLevel.random.nextInt(matches.size())), player);
        }
    }

    private static List<BlockPos> matchingNeighbors(ServerLevel serverLevel, ServerPlayer player, BlockPos center, Block block) {
        List<BlockPos> matches = new ArrayList<>();
        for (BlockPos neighbor : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
            BlockState state = serverLevel.getBlockState(neighbor);
            if (!neighbor.equals(center)
                    && state.is(block)
                    && player.hasCorrectToolForDrops(state)
                    && serverLevel.getBlockEntity(neighbor) == null
                    && serverLevel.mayInteract(player, neighbor)) {
                matches.add(neighbor.immutable());
            }
        }
        return matches;
    }

    private static void echoBreak(ServerLevel serverLevel, BlockPos pos, ServerPlayer player) {
        BlockState state = serverLevel.getBlockState(pos);
        Block.dropResources(state, serverLevel, pos, null, player, player.getMainHandItem());
        serverLevel.destroyBlock(pos, false, player);
    }
}
