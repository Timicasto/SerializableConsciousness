package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.StackSlotHandler;
import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.api.utils.DataUtils;
import cc.sukazyo.sericons.block.multiblocks.MultiblockMetalSmelter;
import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.register.RegistryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetalSmelterTileEntity extends MultiBlockMachineTileEntity<MetalSmelterTileEntity, MetalSmelterRecipe> /*implements IContainerFactory*/ {
    // This Field is Only for Testing.
    boolean first = true;
    public MetalSmelterTileEntity() {
        super(new int[] {5, 6, 5}, RegistryBlocks.METAL_SMELTER, 32000, true, MultiblockMetalSmelter.INSTANCE);
    }

    public List<ItemStack> slots = new ArrayList<>();

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

    @Override
    public void tick() {
        super.tick();
        if (first) {
            SeriConsMod.LOGGER.info("Created MetalSmelter TileEntity, This Log is for Testing");
            first = false;
        }
        if (!level.isClientSide && !isDummy() && !disabled()) {
            if (this.queue.size() < this.maxQueueLength()) {
                Set<Integer> usedSlots = new HashSet<>();
                for (Process<MetalSmelterRecipe> process : queue) {
                    if (process instanceof ProcessInside) {
                        for (int i : ((ProcessInside<MetalSmelterRecipe>) process).inSlots) {
                            usedSlots.add(i);
                        }
                    }
                }
                if (!usedSlots.contains(0)) {
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

//    Inventory inventory = new Inventory(4);

    /*@Override
    public AbstractContainerMenu create(int p_create_1_, Inventory p_create_2_) {
        return null;
    }*/

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
        return new int[0];
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
        return new int[0];
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
        return 0;
    }

    @Override
    public void render(int slot) {

    }

    @Override
    public boolean canInsert(int slot, ItemStack tar) {
        return false;
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

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            MetalSmelterTileEntity trigger = trigger();
            if (trigger == null) {
                return null;
            }
            switch (pos) {
                case 10 :
                    return LazyOptional.of(() -> (T)out);
                case 137:
                    return LazyOptional.of(() -> (T)ore);
                case 138:
                    return LazyOptional.of(() -> (T)changer);
                case 14:
                    return LazyOptional.of(() -> (T)fuel);
            }
        }
        return super.getCapability(cap, side);
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
}
