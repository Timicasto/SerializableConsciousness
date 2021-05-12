package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.register.RegistryItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class FeldsparBlock extends Block {

    public FeldsparBlock() {
        super(Properties.of(Material.STONE).strength(3.0f));
    }

    @Deprecated
    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> drops = new ArrayList<>();
        if (RANDOM.nextBoolean()) {
            drops.add(new ItemStack(RegistryItems.FELDSPAR, RANDOM.nextInt(7) + 1));
        }
        return drops;
    }
}
