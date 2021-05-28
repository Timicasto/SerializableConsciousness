package cc.sukazyo.sericons.crafting;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MetalSmelterRecipe extends MachineRecipe {
    public static final float energyMultiplier = 1;
    public static final float timeMultiplier = 1;
    public final ItemStack mainInput;
    public final ItemStack changer;
    public final ItemStack output;
    public final ItemStack slag;

    public static List<MetalSmelterRecipe> recipes = new ArrayList<>();

    public MetalSmelterRecipe(ItemStack out, ItemStack mainIn, ItemStack changer, ItemStack slag, int time, int power) {
        this.output = out;
        this.mainInput = mainIn;
        this.changer = changer;
        this.slag = slag;
        this.totalTime = (int)Math.floor(time * timeMultiplier);
        this.energy = (int)Math.floor(power * energyMultiplier) * totalTime;
        this.inputs = Lists.newArrayList(this.inputs);
        NonNullList<ItemStack> outs = NonNullList.create();
        outs.add(0, output);
        this.outputs = outs;
    }

    @Override
    public int timeMultiplier() {
        return 0;
    }

    @Override
    public CompoundTag writeData(CompoundTag tag) {
        tag.put("input", mainInput.serializeNBT());
        tag.put("slag", slag.serializeNBT());
        tag.put("changer", changer.serializeNBT());
        tag.put("output", output.serializeNBT());
        return tag;
    }

    public static MetalSmelterRecipe register(ItemStack out, ItemStack mainIn, ItemStack changer, ItemStack slag, int time, int power) {
        MetalSmelterRecipe recipe = new MetalSmelterRecipe(out, mainIn, changer, slag, time, power);
        if (recipe.mainInput != null) {
            recipes.add(recipe);
        }
        return recipe;
    }

    public boolean valid(ItemStack stack) {
        return this.mainInput != null && this.mainInput.equals(stack);
    }

    public static MetalSmelterRecipe searchByIn(ItemStack stack) {
        for (MetalSmelterRecipe recipe : recipes) {
            if (recipe != null && recipe.valid(stack)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean isValidIn(ItemStack stack) {
        for (MetalSmelterRecipe recipe : recipes) {
            if (recipe != null && recipe.valid(stack)) {
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> outputs(ItemStack input) {
        List<ItemStack> ret = new ArrayList<>();
        ret.add(output);
        return outputs;
    }
}
