package cc.sukazyo.sericons.api.multiblock;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * An interface implemented by machines that can handle recipes
 */
public interface IMultiBlockRecipe {
    List<ItemStack> itemInputs();
    List<FluidStack> fluidInputs();
    NonNullList<ItemStack> itemOutputs();

    default NonNullList<ItemStack> actualOutputs(BlockEntity tile) {
        return itemOutputs();
    }

    List<FluidStack> fluidOutputs();

    default List<FluidStack> actualFluidOutputs(BlockEntity tile) {
        return fluidOutputs();
    }

    int time();
    int energy();
    int timeMultiplier();

    CompoundTag writeData(CompoundTag tag);
}
