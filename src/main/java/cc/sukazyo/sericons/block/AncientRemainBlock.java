package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.register.RegistryItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class AncientRemainBlock extends Block {
    public AncientRemainBlock() {
        super(Properties.of(Material.STONE));
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(RegistryItems.BIONIC_BODY_COMPONENT, RANDOM.nextInt(4)));
        return list;
    }
}
