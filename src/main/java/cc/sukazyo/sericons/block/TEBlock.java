package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.api.IItemSlotWrapper;
import cc.sukazyo.sericons.api.multiblock.IEnumPropertyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class TEBlock<E extends Enum<E> & IEnumPropertyBlock> extends TemplateBlock<E> {
    public TEBlock(String name, Material material, EnumProperty<E> property, Object... additional) {
        super(name, material, property, additional);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        return super.getDrops(p_220076_1_, p_220076_2_);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null) {
            for (ItemStack s : ((IItemSlotWrapper) entity).getDrops()) {
                if (!s.isEmpty()) {
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), s));
                } else if (!entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).equals(LazyOptional.empty())) {
                    IItemHandler handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().get();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        if (!handler.getStackInSlot(i).isEmpty()) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i)));
                            handler.extractItem(i, handler.getStackInSlot(i).getCount(), false);
                        }
                    }
                }
            }
        }
        super.spawnAfterBreak(state, world, pos, stack);
        world.removeBlockEntity(pos);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        Item item = Item.byBlock(this);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, 1, NbtUtils.writeBlockState(world.getBlockState(pos)));
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
        super.triggerEvent(state, world, pos, id, param);
        BlockEntity tile = world.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(id, param);
    }

    protected Direction defaultFacing() {
        return Direction.NORTH;
    }


}















