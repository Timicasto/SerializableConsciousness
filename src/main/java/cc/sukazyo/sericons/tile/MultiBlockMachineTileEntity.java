package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.api.IItemSlotWrapper;
import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.api.energy.EnumModeProperty;
import cc.sukazyo.sericons.api.energy.IEnergyConnector;
import cc.sukazyo.sericons.api.multiblock.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiBlockMachineTileEntity<T extends MultiBlockMachineTileEntity<T>> extends MultiBlockPartTileEntity<T> implements IItemSlotWrapper, IToolboxInteractive, IMultiDirectional, IEnergyConnector {
    public final EnergyWrapper.CustomEnergyStorage energyStorage;
    protected final boolean canBeControlled;
    protected boolean invertControl;
    protected final MultiBlockRegistryHandler.IMultiblock instance;
    public int controlTrigger = 0;
    public boolean triggerOn;

    public MultiBlockMachineTileEntity(BlockEntityType<?> type, int[] structureDims, int energyStorageCapacity, boolean canBeControlled, MultiBlockRegistryHandler.IMultiblock instance) {
        super(type, structureDims);
        this.energyStorage = new EnergyWrapper.CustomEnergyStorage(energyStorageCapacity);
        this.canBeControlled = canBeControlled;
        this.instance = instance;
    }

    // END BASE CONSTRUCTING

    // START DATA PROCESSING

    @Override
    public void readCustomNBT(CompoundTag nbt, boolean desc) {
        super.readCustomNBT(nbt, desc);
        energyStorage.deserializeNBT(nbt);
        invertControl = nbt.getBoolean("invertSignal");
        if (desc) {
            controlTrigger = nbt.getBoolean("controled") ? 1 : 0;
            triggerOn = nbt.getBoolean("rson");
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag nbt, boolean desc) {
        super.writeCustomNBT(nbt, desc);
        energyStorage.serializeNBT(nbt);
        nbt.putBoolean("invertSignal", invertControl);
        if (desc) {
            nbt.putBoolean("controled", controlTrigger > 0);
            nbt.putBoolean("rson", triggerOn);
        }
    }



    // END DATA PROCESSING

    // START ENERGY HANDLE
    public abstract int[] energyPos();

    public boolean isEnergyPos() {
        for (int i : energyPos()) {
            if (pos == i) {
                return true;
            }
        }
        return false;
    }

    public EnergyWrapper.CustomEnergyStorage getEnergyStorage() {
        T trigger = this.trigger();
        if (trigger != null) {
            return trigger.energyStorage;
        }
        return energyStorage;
    }

    @NotNull
    @Override
    public EnumModeProperty getSideMode(Direction facing) {
        return this.formed && this.isEnergyPos() ? EnumModeProperty.INPUT : EnumModeProperty.BLOCK;
    }

    EnergyWrapper.EnergyWrapperImpl wrapper = new EnergyWrapper.EnergyWrapperImpl(this, null);

    public EnergyWrapper.EnergyWrapperImpl getWrapper() {
        return this.formed && this.isEnergyPos() ? wrapper : null;
    }

    @Override
    public void notifyTransferUpdate(int energy, boolean simulate) {
        if (!simulate) {
            this.updateTrigger(null, energy != 0);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (!isDummy()) {
            BlockPos nullPos = this.getPosFromIndex(0);
            return new AABB(nullPos, nullPos.relative(facing, structureDims[0]).relative(mirrored ? facing.getCounterClockWise() : facing.getClockWise(), structureDims[2]).above(structureDims[1]));
        }
        return super.getRenderBoundingBox();
    }

    // END ENERGY HANDLE

    // START REDSTONE CONTROL
    public abstract int[] redstonePos();

    public boolean isRedstonePos() {
        if (!canBeControlled || redstonePos() == null) {
            return false;
        }
        for (int i : redstonePos()) {
            if (pos == i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean interact(Direction facing, Player monitor, float x, float y, float z) {
        if (this.isRedstonePos()) {
            MultiBlockMachineTileEntity<T> trigger = trigger();
            trigger.invertControl = !trigger.invertControl;
            updateTrigger(null, true);
            return true;
        }
        return false;
    }

    public boolean disabled() {
        if (controlTrigger > 0 && !triggerOn) {
            return true;
        }
        int[] redstonePos = redstonePos();
        if (redstonePos == null || redstonePos.length < 1) {
            return false;
        }
        for (int pos : redstonePos) {
            T tile = getTileFromIndex(pos);
            if (tile != null) {
                boolean flag = level.getBestNeighborSignal(tile.getBlockPos()) > 0;
                return invertControl != flag;
            }
        }
        return false;
    }

    // END REDSTONE CONTROL

    // START POSITION MANAGEMENT

    @Nullable
    @SuppressWarnings("unchecked")
    public T getTileFromIndex(int index) {
        BlockPos tar = getPosFromIndex(index);
        BlockEntity te = level.getBlockEntity(tar);
        if (this.getClass().isInstance(te)) {
            return (T)te;
        }
        return null;
    }

    @Override
    public ItemStack getBlock() {
        if (pos < 0) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = ItemStack.EMPTY;
        try {
            int blocksPerLevel = structureDims[0] * structureDims[2];
            int y = (pos / blocksPerLevel);
            int x = (pos % blocksPerLevel / structureDims[2]);
            int z = (pos % structureDims[2]);
            stack = new ItemStack(Item.byBlock(this.instance.getStructure()[x][y][z].getBlock()), 1, NbtUtils.writeBlockState(this.instance.getStructure()[x][y][z]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stack.copy();
    }

    @Override
    public boolean isZMirrored() {
        return this.mirrored;
    }

    // END POSITION MANAGEMENT




}
