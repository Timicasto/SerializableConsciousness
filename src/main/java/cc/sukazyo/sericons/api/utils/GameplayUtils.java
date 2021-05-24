package cc.sukazyo.sericons.api.utils;

import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GameplayUtils {

    public static BlockState getStateFromIS(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Block block = Block.byItem(stack.getItem());
        if (block != null) {
            return NbtUtils.readBlockState(stack.getOrCreateTag());
        }
        return null;
    }
}
