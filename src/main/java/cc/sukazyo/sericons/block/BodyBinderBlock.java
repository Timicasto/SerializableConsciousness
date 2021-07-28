package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.register.RegistryItems;
import cc.sukazyo.sericons.tile.BodyBinderTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BodyBinderBlock extends Block {
    public BodyBinderBlock() {
        super(Properties.of(Material.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new BodyBinderTileEntity();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof BodyBinderTileEntity) {
            if (player.getItemInHand(hand).getItem() == RegistryItems.BIONIC_BODY_COMPONENT) {
                ((BodyBinderTileEntity)entity).component.insertItem(0, new ItemStack(RegistryItems.BIONIC_BODY_COMPONENT, 1), false);
                player.getItemInHand(hand).shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
