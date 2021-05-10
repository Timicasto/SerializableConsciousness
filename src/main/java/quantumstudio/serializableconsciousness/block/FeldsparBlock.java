package quantumstudio.serializableconsciousness.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import quantumstudio.serializableconsciousness.ContentHandler;

import java.util.ArrayList;
import java.util.List;

public class FeldsparBlock extends Block {
    public FeldsparBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(3));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        if (RANDOM.nextBoolean()) {
            drops.add(new ItemStack(ContentHandler.feldsparUglyDustItem.get(), RANDOM.nextInt(7) + 1));
        }
        return drops;
    }
}
