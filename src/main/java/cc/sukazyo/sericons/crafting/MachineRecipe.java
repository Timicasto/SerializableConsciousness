package cc.sukazyo.sericons.crafting;

import cc.sukazyo.sericons.api.multiblock.IMultiBlockRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class MachineRecipe implements IMultiBlockRecipe {
    protected List<ItemStack> inputs;

    @Override
    public List<ItemStack> itemInputs() {
        return inputs;
    }

    protected NonNullList<ItemStack> outputs;

    @Override
    public NonNullList<ItemStack> itemOutputs() {
        return outputs;
    }

    protected List<FluidStack> fluidIn;

    @Override
    public List<FluidStack> fluidInputs() {
        return fluidIn;
    }

    protected List<FluidStack> fluidOut;

    @Override
    public List<FluidStack> fluidOutputs() {
        return fluidOut;
    }

    int totalTime;

    @Override
    public int time() {
        return totalTime;
    }

    int energy;

    @Override
    public int energy() {
        return energy;
    }
}
