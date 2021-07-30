package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.tile.BoilerTileEntity;
import cc.sukazyo.sericons.tile.GeneratorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlock extends Block {
    public GeneratorBlock() {
        super(Properties.of(Material.METAL).strength(2.5F).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new GeneratorTileEntity();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity te = level.getBlockEntity(pos);
        if (player.getItemInHand(hand).getItem() == Items.STICK) {
            GeneratorTileEntity boiler = (GeneratorTileEntity)te;
            SeriConsMod.LOGGER.info("K: " + boiler.kinetic.getkCurrent() + ", FE: " + boiler.storage.getEnergyStored());
        }
        return InteractionResult.SUCCESS;
    }
}
