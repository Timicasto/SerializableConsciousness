package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.tile.BoilerTileEntity;
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
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class BoilerBlock extends Block {
    public BoilerBlock() {
        super(Properties.of(Material.METAL).strength(3F).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return RegistryBlocks.BOILER_TILE_ENTITY.create();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity te = level.getBlockEntity(pos);
        if (player.getItemInHand(hand).getItem() == Items.COAL) {
            player.getItemInHand(hand).shrink(1);
            ((BoilerTileEntity)te).insertFuel();
        }
        if (player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL) {
            player.getItemInHand(hand).setDamageValue(player.getItemInHand(hand).getDamageValue() + 1);
            ((BoilerTileEntity)te).start();
        }
        if (player.getItemInHand(hand).getItem() == Items.WATER_BUCKET) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            ((BoilerTileEntity)te).tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
        }
        if (player.getItemInHand(hand).getItem() == Items.STICK) {
            BoilerTileEntity boiler = (BoilerTileEntity)te;
            SeriConsMod.LOGGER.info("water: " + boiler.tank.getFluidInTank(0).getAmount() + ", steam: " + boiler.steam.getFluidInTank(0).getAmount() + ", fuel: " + boiler.fuel + ", State: " + boiler.running);
        }
        return InteractionResult.SUCCESS;
    }
}
