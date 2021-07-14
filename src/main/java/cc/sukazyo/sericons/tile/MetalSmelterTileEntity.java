package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.api.utils.DataUtils;
import cc.sukazyo.sericons.block.multiblocks.MultiblockMetalSmelter;
import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.inventory.MetalSmelterMenu;
import cc.sukazyo.sericons.register.RegistryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MetalSmelterTileEntity extends MultiBlockMachineTileEntity<MetalSmelterTileEntity> implements MenuProvider {
    // This Field is Only for Testing.
    boolean first = true;

    private int progress;
    private int validFuel;
    private boolean isWorking;
    private boolean isTempIncreasing;
    private int temp;
    public Random random = new Random();
    public Container container = new SimpleContainer(4);


    public MetalSmelterTileEntity() {
        super(RegistryBlocks.METAL_SMELTER, new int[]{5, 6, 5}, 32000, true, MultiblockMetalSmelter.INSTANCE);
        slots.addAll(Arrays.asList(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY));
    }

    public List<ItemStack> slots = new ArrayList<>(4);

    @Override
    public void readCustomNBT(CompoundTag nbt, boolean desc) {
        super.readCustomNBT(nbt, desc);
        if (!desc) {
            slots = DataUtils.readSlots(nbt.getList("Slots", 10), 26);
        }

    }

    @Override
    public void writeCustomNBT(CompoundTag nbt, boolean desc) {
        super.writeCustomNBT(nbt, desc);
        if (!desc) {
            nbt.put("Slots", DataUtils.writeSlots(slots));
        }
    }

    public MetalSmelterItemIntArray array = new MetalSmelterItemIntArray();

    @Override
    public void tick() {
        if (first) {
            SeriConsMod.LOGGER.info("Created MetalSmelter TileEntity, This Log is for Testing");
            first = false;
        }
        if (!level.isClientSide) {
            this.array.set(0, this.slots.get(0).getCount());
            this.array.set(1, this.slots.get(1).getCount());
            this.array.set(2, this.slots.get(2).getCount());
            this.array.set(3, this.slots.get(3).getCount());
            ItemStack input = ore.getStackInSlot(0);
            if (isTempIncreasing) {
                if (validFuel > 0) {
                    ++temp;
                    --validFuel;
                } else {
                    isTempIncreasing = false;
                }
            }
            if (temp >= 950) {
                isWorking = true;
                isTempIncreasing = false;
            } else {
                isWorking = false;
            }

            if (validFuel <= 1) {
                if (fuel.getStackInSlot(0).getItem() == MetalSmelterRecipe.getChanger(input).getItem() && fuel.getStackInSlot(0).getCount() >= 1) {
                    fuel.extractItem(0, 1, false);
                    setChanged();
                    validFuel += 3200;
                }else {
                    return;
                }
            }


            if (isWorking && validFuel > 0 && !isTempIncreasing) {
                ItemStack resutl = MetalSmelterRecipe.getResult(input);
                ++progress;
                --validFuel;
                if (progress >= MetalSmelterRecipe.getTime(input) + random.nextInt(200)) {
                    int count = resutl.getCount() - random.nextInt(resutl.getCount() - 1);
                    slag.insertItem(0, MetalSmelterRecipe.getSlag(input), false);
                    BlockPos pos = getPosFromIndex(10).offset(0, -1, 0).relative(facing.getOpposite(), 2);
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(resutl.getItem(), count)));
                    ore.extractItem(0, 1, false);
                    fuel.extractItem(0, MetalSmelterRecipe.getChanger(input).getCount(), false);
                    setChanged();
                    progress = 0;
                }
                SeriConsMod.LOGGER.info("current out : " + resutl.toString());
            }
        }
    }

    public void makeTempIncrease() {
        isTempIncreasing = true;
    }

    @Override
    public int[] energyPos() {
        return new int[0];
    }

    @Override
    public int[] redstonePos() {
        return new int[] {137};
    }

    @Override
    public List<ItemStack> slots() {
        return this.slots;
    }

    @Override
    public int getMaxStackSize(int slot) {
        return 8;
    }

    @Override
    public void render(int slot) {

    }

    @Override
    public boolean canInsert(int slot, ItemStack tar) {
        return true;
    }

    @Override
    public EnergyWrapper.EnergyWrapperImpl getWrappedCapability(Direction facing) {
        return null;
    }

    @NotNull
    @Override
    protected IFluidTank[] getAccessibleFluidTanks(Direction side) {
        return new IFluidTank[0];
    }

    @Override
    protected boolean canFillFrom(int tank, Direction side, FluidStack resource) {
        return false;
    }

    @Override
    protected boolean canDrainFrom(int tank, Direction side) {
        return false;
    }

    @Override
    public float[] getBlockBound() {
        return new float[0];
    }

    ItemStackHandler ore = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 8;
        }
    };
    ItemStackHandler changer = new ItemStackHandler(1);
    ItemStackHandler fuel = new ItemStackHandler(1);
    ItemStackHandler slag = new ItemStackHandler(1);

    public ItemStackHandler getItemHandler(int index) {
        switch (index) {
            case 0:
                return ore;
            case 1:
                return changer;
            case 2:
                return fuel;
            case 3:
                return slag;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            MetalSmelterTileEntity trigger = trigger();
            if (trigger == null) {
                return LazyOptional.empty();
            }
            switch (pos) {
                case 10:
                    return LazyOptional.of(() -> (T) slag);
                case 137:
                    return LazyOptional.of(() -> (T) ore);
                case 138:
                    return LazyOptional.of(() -> (T) changer);
                case 14:
                    return LazyOptional.of(() -> (T) fuel);
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return TextComponent.EMPTY;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        container = new SimpleContainer(slots.toArray(new ItemStack[]{}));
        return new MetalSmelterMenu(containerId, inv, container, this, array);
    }


    public static class MetalSmelterItemIntArray implements ContainerData {
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;

        @Override
        public int get(int i) {
            switch (i) {
                case 0:
                    return this.i1;
                case 1:
                    return this.i2;
                case 2:
                    return this.i3;
                case 3:
                    return this.i4;
            }
            return 0;
        }

        @Override
        public void set(int i, int j) {
            switch (i) {
                case 0:
                    this.i1 = j;
                case 1:
                    this.i2 = j;
                case 2:
                    this.i3 = j;
                case 3:
                    this.i4 = j;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
