package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.api.multiblock.IArrayBoundBlock;
import cc.sukazyo.sericons.api.multiblock.ITileWithFacing;
import cc.sukazyo.sericons.api.utils.GameplayUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class MultiBlockPartTileEntity<T extends MultiBlockPartTileEntity<T>> extends TemplateTileEntity implements TickableBlockEntity, ITileWithFacing, IArrayBoundBlock {
    public boolean formed = false;
    public int pos = -1;
    public int[] offset = {0, 0, 0};
    public boolean mirrored = false;
    public Direction facing = Direction.NORTH;
    // Prevents Half or Duplicate Disassembly When Working With A Tool That Can Break Block By A Certain Range
    public long onlyLocalDisasm = -1;
    // X Y Z
    protected final int[] structureDims;

    protected MultiBlockPartTileEntity(BlockEntityType<?> type, int[] structureDims) {
        super(type);
        this.structureDims = structureDims;
    }

    //START BASE IMPLEMENTING

    @Override
    public Direction getFacing() {
        return this.facing;
    }

    @Override
    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    @Override
    public int getFacingLimit() {
        return 2;
    }

    @Override
    public boolean mirrorFacing(LivingEntity placer) {
        return false;
    }

    @Override
    public boolean canBeRotated(Direction side, float x, float y, float z, LivingEntity entity) {
        return false;
    }

    @Override
    public boolean canRotate(Direction axis) {
        return false;
    }

    // END BASE IMPLEMENTING

    // START DATA PROCESSING


    @Override
    public void readCustomNBT(CompoundTag nbt, boolean desc) {
        formed = nbt.getBoolean("formed");
        pos = nbt.getInt("pos");
        offset = nbt.getIntArray("offset");
        mirrored = nbt.getBoolean("mirrored");
        facing = Direction.from3DDataValue(nbt.getInt("facing"));
    }

    @Override
    public void writeCustomNBT(CompoundTag nbt, boolean desc) {
        nbt.putBoolean("formed", formed);
        nbt.putInt("pos", pos);
        nbt.putIntArray("offset", offset);
        nbt.putBoolean("mirrored", mirrored);
        nbt.putInt("facing", facing.ordinal());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != null && this.getAccessibleFluidTanks(side).length > 0) {
            return LazyOptional.of(() -> (T)new FluidWrapperForMultiBlock(this, side));
        }
        return super.getCapability(cap, side);
    }

    // END DATA PROCESSING

    // START FLUID MANAGEMENT
    @Nonnull
    protected abstract IFluidTank[] getAccessibleFluidTanks(Direction side);
    protected abstract boolean canFillFrom(int tank, Direction side, FluidStack resource);
    protected abstract boolean canDrainFrom(int tank, Direction side);

    public static class FluidWrapperForMultiBlock implements IFluidHandler {
        final MultiBlockPartTileEntity te;
        final Direction facing;

        public FluidWrapperForMultiBlock(MultiBlockPartTileEntity te, Direction side) {
            this.te = te;
            this.facing = side;
        }

        @Override
        public int getTanks() {
            return this.te.getAccessibleFluidTanks(facing).length;
        }

        @NotNull
        @Override
        public FluidStack getFluidInTank(int i) {
            if (!this.te.formed) {
                return FluidStack.EMPTY;
            }
            IFluidTank[] tanks = this.te.getAccessibleFluidTanks(facing);
            return tanks[i].getFluid();
        }

        @Override
        public int getTankCapacity(int i) {
            if (!this.te.formed) {
                return 0;
            }
            IFluidTank[] tanks = this.te.getAccessibleFluidTanks(facing);
            return tanks[i].getCapacity();
        }

        @Override
        public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
            return this.te.canFillFrom(i, facing, fluidStack);
        }


        @Override
        public int fill(FluidStack fluidStack, FluidAction fluidAction) {
            if (!this.te.formed || fluidStack == null) {
                return 0;
            }
            IFluidTank[] tanks = this.te.getAccessibleFluidTanks(facing);
            int fill = -1;
            for (int i = 0 ; i < tanks.length ; i++) {
                IFluidTank tank = tanks[i];
                if (tank != null && this.te.canFillFrom(i, facing, fluidStack) && tank.getFluid() != null && tank.getFluid().isFluidEqual(fluidStack)) {
                    fill = tank.fill(fluidStack, fluidAction);
                    if (fill > 0) {
                        break;
                    }
                }
            }
            if (fill == -1) {
                for (int i = 0 ; i < tanks.length ; i++) {
                    IFluidTank tank = tanks[i];
                    if (tank != null && this.te.canFillFrom(i, facing, fluidStack)) {
                        fill = tank.fill(fluidStack, fluidAction);
                        if (fill > 0) {
                            break;
                        }
                    }
                }
            }
            if (fill > 0) {
                this.te.updateTrigger(null, true);
            }
            return fill < 0 ? 0 : null;
        }

        @NotNull
        @Override
        public FluidStack drain(int i, FluidAction fluidAction) {
            if (!this.te.formed || i == 0) {
                return FluidStack.EMPTY;
            }
            IFluidTank[] tanks = this.te.getAccessibleFluidTanks(facing);
            FluidStack drain = null;
            for (int j = 0; j < tanks.length; j++) {
                IFluidTank tank = tanks[i];
                if (tank != null && this.te.canDrainFrom(i, facing)) {
                    drain = tank.drain(i, fluidAction);
                    if (drain != null) {
                        break;
                    }
                }
            }
            if (drain != null) {
                this.te.updateTrigger(null, true);
            }
            return drain;
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
            if (!this.te.formed || fluidStack == null) {
                return FluidStack.EMPTY;
            }
            IFluidTank[] tanks = this.te.getAccessibleFluidTanks(facing);
            FluidStack drain = null;
            for (int i = 0; i < tanks.length; i++) {
                IFluidTank tank = tanks[i];
                if (tank != null && this.te.canDrainFrom(i, facing)) {
                    if (tank instanceof IFluidHandler) {
                        drain = ((IFluidHandler)tank).drain(fluidStack, fluidAction);
                    } else {
                        drain = tank.drain(fluidStack.getAmount(), fluidAction);
                    }
                    if (drain != FluidStack.EMPTY) {
                        break;
                    }
                }
            }
            if (drain != null) {
                this.te.updateTrigger(null, true);
            }
            return drain;
        }
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
    }

    public static boolean immovable() {
        return true;
    }

    @Nullable
    public T trigger() {
        if (offset[0] == 0 && offset[1] == 0 && offset[2] == 0) {
            return (T)this;
        }
        BlockPos pos = getBlockPos().offset(-offset[0], -offset[1], -offset[2]);
        BlockEntity te = level.getBlockEntity(pos);
        return this.getClass().isInstance(te) ? (T)te : null;
    }

    public void updateTrigger(BlockState state, boolean update) {
        T trigger = trigger();
        if (trigger != null) {
            trigger.setChanged();
            if (update) {
                trigger.markContainingUpdate(state);
            }
        }
    }

    public boolean isDummy() {
        return offset[0] != 0 || offset[1] != 0 || offset[2] != 0;
    }

    public abstract ItemStack getBlock();
    public void disassemble() {
        if (formed && !level.isClientSide) {
            BlockPos origin = getOrigin();
            BlockPos trigger = getBlockPos().offset(-offset[0], -offset[1], -offset[2]);
            long time = level.getGameTime();
            for (int x = 0; x < structureDims[0]; x++) {
                for (int y = 0; y < structureDims[1]; y++) {
                    for (int z = 0; z < structureDims[2]; z++) {
                        int zz = mirrored ? -z : z;
                        BlockPos pos = origin.relative(facing, x).relative(facing.getClockWise(), zz).offset(0, y, 0);
                        ItemStack stack = ItemStack.EMPTY;
                        BlockEntity te = level.getBlockEntity(pos);
                        if (te instanceof MultiBlockPartTileEntity) {
                            MultiBlockPartTileEntity entity = (MultiBlockPartTileEntity)te;
                            Vec3i delta = pos.subtract(trigger);
                            if (entity.offset[0] != delta.getX() || entity.offset[1] != delta.getY() || entity.offset[2] != delta.getZ()) {
                                continue;
                            } else if (time != entity.onlyLocalDisasm) {
                                stack = ((MultiBlockPartTileEntity<?>) te).getBlock();
                                entity.formed = false;
                            }
                        }
                        if (pos.equals(getBlockPos())) {
                            stack = this.getBlock();
                        }
                        BlockState state = GameplayUtils.getStateFromIS(stack);
                        if (state != null) {
                            if (state != null) {
                                if (pos.equals(getBlockPos())) {
                                    level.addFreshEntity(new ItemEntity(level, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
                                } else {
                                    replace(pos, state, stack, x, y, z);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public BlockPos getOrigin() {
        return getPosFromIndex(0);
    }

    public BlockPos getPosFromIndex(int target) {
        int xz = structureDims[0] * structureDims[2];
        int deltaY = (target / xz) - (pos / xz);
        int deltaX = (target % xz / structureDims[2]) - (pos % xz / structureDims[2]);
        int deltaZ = (target % structureDims[2]) - (pos % structureDims[2]);
        int z = mirrored ? -deltaZ : deltaZ;
        return getBlockPos().relative(facing, deltaX).relative(facing.getClockWise(), z).offset(0, deltaY, 0);
    }

    public void replace(BlockPos pos, BlockState state, ItemStack stack, int x, int y, int z) {
        if (state.getBlock() == this.getBlockState().getBlock()) {
            level.destroyBlock(pos, true);
        }
        level.setBlock(pos, state, 0);
    }
}
