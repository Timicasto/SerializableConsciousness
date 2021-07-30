package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.tile.BoilerTileEntity;
import cc.sukazyo.sericons.tile.SteamTurbineTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTurbineBlock extends Block {
    public SteamTurbineBlock() {
        super(Properties.of(Material.METAL).strength(2.5F).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return RegistryBlocks.STEAM_TURBINE_TILE_ENTITY.create();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity te = level.getBlockEntity(pos);
        System.out.println("K: " + ((SteamTurbineTileEntity)te).storage.getkCurrent());
        return InteractionResult.SUCCESS;
    }
}
