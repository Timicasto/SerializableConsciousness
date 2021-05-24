package cc.sukazyo.sericons.item;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.multiblock.MultiBlockRegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class FeldsparUglyDustItem extends Item {

    public FeldsparUglyDustItem(@Nonnull Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        SeriConsMod.LOGGER.info("Used Item");
        return form(context.getPlayer(), context.getLevel(), context.getClickedPos(), context.getHorizontalDirection(), context.getHand());
    }

    InteractionResult form(Player player, Level world, BlockPos pos, Direction side, InteractionHand hand) {
        SeriConsMod.LOGGER.info("Trying to form");
        for (MultiBlockRegistryHandler.IMultiblock multiblock : MultiBlockRegistryHandler.multiblocks) {
            SeriConsMod.LOGGER.info("Finding structure");
            if (multiblock.isTrigger(world.getBlockState(pos))) {
                if (multiblock.createStructure(world, pos, side, player)) {
                    SeriConsMod.LOGGER.info("Forming structure");
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
