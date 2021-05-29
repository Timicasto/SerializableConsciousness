package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.StackSlotHandler;
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
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.asm.RuntimeEnumExtender;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MetalSmelterTileEntity extends MultiBlockMachineTileEntity<MetalSmelterTileEntity, MetalSmelterRecipe> implements MenuProvider{
    // This Field is Only for Testing.
    boolean first = true;



    public MetalSmelterTileEntity() {
        super(RegistryBlocks.METAL_SMELTER, new int[]{5, 6, 5}, 32000, true, MultiblockMetalSmelter.INSTANCE);
        slots.addAll(Arrays.asList(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY));
    }

    public List<ItemStack> slots = new ArrayList<>(4);

    @Override
    public void readCustomNBT(CompoundTag nbt, boolean desc) {
        super.readCustomNBT(nbt, desc);
        if (!desc) {
            slots = DataUtils.readSlots(nbt.getList("slots", 10), 26);
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
        super.tick();
        if (first) {
            SeriConsMod.LOGGER.info("Created MetalSmelter TileEntity, This Log is for Testing");
            first = false;
        }
        if (!level.isClientSide) {
            this.array.set(0, this.slots.get(0).getCount());
            this.array.set(1, this.slots.get(1).getCount());
            this.array.set(2, this.slots.get(2).getCount());
            this.array.set(3, this.slots.get(3).getCount());
            if (!isDummy() && !disabled()) {
                if (this.queue.size() < this.maxQueueLength()) {
                    Set<Integer> usedSlots = new HashSet<>();
                    for (Process<MetalSmelterRecipe> process : queue) {
                        if (process instanceof ProcessInside) {
                            for (int i : ((ProcessInside<MetalSmelterRecipe>) process).inSlots) {
                                usedSlots.add(i);
                            }
                        }
                    }
                    if (!slots.isEmpty() && !usedSlots.contains(0)) {
                        ItemStack stack = this.slots.get(0);
                        MetalSmelterRecipe recipe = MetalSmelterRecipe.searchByIn(stack);
                        if (recipe != null) {
                            MetalSmelterProcess process = new MetalSmelterProcess(recipe, 0, 1, 2);
                            if (this.addTask(process, true)) {
                                this.addTask(process, false);
                                usedSlots.add(0);
                            }
                        }
                    }
                }
            }
        }
    }




    @Override
    protected MetalSmelterRecipe readRecipe(CompoundTag tag) {
        return null;
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
    public IFluidTank[] tanks() {
        return new IFluidTank[0];
    }

    @Override
    public MetalSmelterRecipe searchRecipe(ItemStack insertion) {
        return null;
    }

    @Override
    public int[] outSlots() {
        return new int[] {3};
    }

    @Override
    public int[] outTanks() {
        return new int[0];
    }

    @Override
    public boolean additionalCheck(Process<MetalSmelterRecipe> process) {
        return true;
    }

    @Override
    public void output(ItemStack out) {
        BlockPos pos = getPosFromIndex(10).offset(0, -1, 0).relative(facing.getOpposite(), 2);
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), out));
    }

    @Override
    public void outFluid(FluidStack out) {

    }

    @Override
    public void pop(Process<MetalSmelterRecipe> process) {
        if (!process.recipe.slag.isEmpty()) {
            BlockPos pos = getPosFromIndex(10).offset(0, -1, 0).relative(facing.getOpposite(), 2);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), process.recipe.slag));
        }
    }

    @Override
    public int maxProcessPerTick() {
        return 8;
    }

    @Override
    public int maxQueueLength() {
        return 8;
    }

    @Override
    public float minProcessDistance(Process process) {
        return 0;
    }

    @Override
    public boolean outSideMachine() {
        return false;
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

    StackSlotHandler ore = new StackSlotHandler(1, this, 0, true, true) {
        @Override
        public int getSlotLimit(int i) {
            return 8;
        }
    };
    StackSlotHandler changer = new StackSlotHandler(1, this, 1, true, false);
    StackSlotHandler fuel = new StackSlotHandler(1, this, 2, true, false);
    StackSlotHandler out = new StackSlotHandler(1, this, 3, false, true);

    public StackSlotHandler getItemHandler(int index) {
        switch (index) {
            case 0:
                return ore;
            case 1:
                return changer;
            case 2:
                return fuel;
            case 3:
                return out;
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
                    return LazyOptional.of(() -> (T) out);
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
        return new MetalSmelterMenu(containerId, inv, this, array);
    }

    public static class MetalSmelterProcess extends ProcessInside<MetalSmelterRecipe> {

        public MetalSmelterProcess(MetalSmelterRecipe recipe, int... inSlots) {
            super(recipe, inSlots);
        }

        @Override
        protected List<ItemStack> outputs(MultiBlockMachineTileEntity te) {
            ItemStack in = te.slots().get(this.inSlots[0]);
            return recipe.outputs(in);
        }
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
