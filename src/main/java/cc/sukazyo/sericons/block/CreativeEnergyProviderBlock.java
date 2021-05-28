package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.register.RegistryBlocks;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class CreativeEnergyProviderBlock extends Block {

    public CreativeEnergyProviderBlock() {
        super(Properties.of(Material.STONE));
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return RegistryBlocks.CREATIVE_ENERGY_PROVIDER.create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
