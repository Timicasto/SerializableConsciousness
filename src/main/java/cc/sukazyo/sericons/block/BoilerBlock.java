package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.tile.BoilerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BoilerBlock extends Block {
    public BoilerBlock() {
        super(Properties.of(Material.METAL));
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new BoilerTileEntity();
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
        return InteractionResult.SUCCESS;
    }
}
