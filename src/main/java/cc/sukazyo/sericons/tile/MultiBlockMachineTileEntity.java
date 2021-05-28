package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.api.IItemSlotWrapper;
import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.api.energy.EnumModeProperty;
import cc.sukazyo.sericons.api.energy.IEnergyConnector;
import cc.sukazyo.sericons.api.multiblock.*;
import cc.sukazyo.sericons.api.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class MultiBlockMachineTileEntity<T extends MultiBlockMachineTileEntity<T, Q>, Q extends IMultiBlockRecipe> extends MultiBlockPartTileEntity<T> implements IItemSlotWrapper, IToolboxInteractive, IMultiDirectional, IEnergyConnector, IMachine {
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
        ListTag processQueue = nbt.getList("processQueue", 10);
        // TODO Clear the queue
        for (int i = 0; i < processQueue.size(); i++) {
            CompoundTag tag = processQueue.getCompound(i);
            IMultiBlockRecipe recipe = readRecipe(tag);
            if (recipe != null) {
                int processTime = tag.getInt("processTime");
                Process process = recipeFromNBT(tag);
                if (process != null) {
                    process.processTime = processTime;
                    queue.add(process);
                }
            }
        }
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
        ListTag queue = new ListTag();
        for (Process process : this.queue) {
            queue.add(serializeProcess(process));
        }
        nbt.put("queue", queue);
        if (desc) {
            nbt.putBoolean("controled", controlTrigger > 0);
            nbt.putBoolean("rson", triggerOn);
        }
    }

    protected abstract Q readRecipe(CompoundTag tag);
    protected Process recipeFromNBT(CompoundTag tag) {
        IMultiBlockRecipe recipe = readRecipe(tag);
        if (recipe != null) {
            if (outSideMachine()) {
                return new ProcessOutside(recipe, tag.getFloat("transPoint"), NonNullList.of(ItemStack.of((CompoundTag) tag.get("inputs"))));
            } else {
                return new ProcessInside(recipe, tag.getIntArray("inSlots")).setInTanks(tag.getIntArray("inTanks"));
            }
        }
        return null;
    }

    protected CompoundTag serializeProcess(Process process) {
        CompoundTag tag = process.recipe.writeData(new CompoundTag());
        tag.putInt("processTime", process.processTime);
        process.serializeAdditionalNBT(tag);
        return tag;
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
            MultiBlockMachineTileEntity<T, Q> trigger = trigger();
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

    // PROCESS MODULE

    public List<Process<Q>> queue = new ArrayList<>();
    public int ticked = 0;

    @Override
    public void tick() {
        ticked = 0;
        if (level.isClientSide || isDummy() || disabled()) {
            return;
        }
        int max = maxProcessPerTick();
        int i = 0;
        Iterator<Process<Q>> it = queue.iterator();
        ticked = 0;
        while (it.hasNext() && i++ < max) {
            Process<Q> process = it.next();
            if (process.canDoBy(this)) {
                process.tick(this);
                ticked++;
            }
            if (process.clear) {
                it.remove();
            }
        }
    }

    public abstract IFluidTank[] tanks();
    public abstract Q searchRecipe(ItemStack insertion);
    public abstract int[] outSlots();
    public abstract int[] outTanks();
    public abstract boolean additionalCheck(Process<Q> process);
    public abstract void output(ItemStack out);
    public abstract void outFluid(FluidStack out);
    public abstract void pop(Process<Q> process);
    public abstract int maxProcessPerTick();
    public abstract int maxQueueLength();
    public abstract float minProcessDistance(Process process);
    public abstract boolean outSideMachine();

    public boolean addTask(Process<Q> process, boolean simulate) {
        return addTask(process, simulate, false);
    }

    @SuppressWarnings("unchecked")
    public boolean addTask(Process<Q> process, boolean simulate, boolean insertToFront) {
        if (insertToFront && process instanceof ProcessOutside) {
            for (Process<Q> qProcess : queue) {
                if (qProcess instanceof ProcessOutside && process.recipe.equals(qProcess.recipe)) {
                    ProcessOutside p = (ProcessOutside)qProcess;
                    boolean canStack = true;
                    for (ItemStack oldIn : (List<ItemStack>)p.inputs) {
                        for (ItemStack in : ((ProcessOutside<Q>) process).inputs) {
                            if (oldIn.getItem() == in.getItem() && oldIn.getOrCreateTag().equals(in.getOrCreateTag()) && oldIn.areCapsCompatible(in)) {
                                if (oldIn.getCount() + (in).getCount() > oldIn.getMaxStackSize()) {
                                    canStack = false;
                                    break;
                                }
                            }
                        }
                        if (!canStack) {
                            break;
                        }
                    }
                    if (canStack) {
                        if (!simulate) {
                            for (ItemStack oldIn : (List<ItemStack>) p.inputs) {
                                for (ItemStack in : (List<ItemStack>) ((ProcessOutside) process).inputs) {
                                    if (oldIn.getItem() == in.getItem() && oldIn.getOrCreateTag().equals(in.getOrCreateTag()) && oldIn.areCapsCompatible(in)) {
                                        oldIn.grow(in.getCount());
                                        break;
                                    }
                                }
                            }
                        }
                        return true;
                    }
                }
            }
        }
        if (maxQueueLength() < 0 || queue.size() < maxQueueLength()) {
            float dist = 1;
            Process<Q> p = null;
            if (queue.size() > 0) {
                p = queue.get(queue.size() - 1);
                if (p != null) {
                    dist = p.processTime / (float)p.maxTime;
                }
            }
            if (p != null && dist < minProcessDistance(p)) {
                return false;
            }
            if (!simulate) {
                queue.add(process);
            }
            return true;
        }
        return false;
    }

    @Override
    public int[] currentStep() {
        T trigger = trigger();
        if (trigger != this && trigger != null) {
            return trigger.currentStep();
        }
        int[] step = new int[queue.size()];
        for (int i = 0; i < step.length; i++) {
            step[i] = queue.get(i).processTime;
        }
        return step;
    }

    @Override
    public int[] maxStep() {
        T trigger = trigger();
        if (trigger != this && trigger != null) {
            return trigger.maxStep();
        }
        int[] steps = new int[queue.size()];
        for (int i = 0; i < steps.length; i++) {
            steps[i] = queue.get(i).maxTime;
        }
        return steps;
    }

    public boolean renderStateOn() {
        return (controlTrigger <= 0 || triggerOn) && getEnergyStorage().getEnergyStored() > 0 && !disabled() && queue.isEmpty();
    }

    // END PROCESS MODULE

    public abstract static class Process<Q extends IMultiBlockRecipe> {
        public Q recipe;
        public int processTime;
        public int maxTime;
        public int energy;
        public boolean clear = false;

        public Process(Q recipe) {
            this.recipe = recipe;
            this.processTime = 0;
            this.maxTime = recipe.time();
            this.energy = recipe.energy() / this.maxTime;
        }

        protected List<ItemStack> outputs(MultiBlockMachineTileEntity te) {
            return recipe.actualOutputs(te);
        }

        protected List<FluidStack> fluidOutputs(MultiBlockMachineTileEntity te) {
            return recipe.actualFluidOutputs(te);
        }

        public boolean canDoBy(MultiBlockMachineTileEntity te) {
            if (te.energyStorage.extractEnergy(energy, true) == energy) {
                List<ItemStack> outputs = recipe.itemOutputs();
                if (outputs != null && !outputs.isEmpty()) {
                    int[] outputSlots = te.outSlots();
                    for (ItemStack output : outputs) {
                        if (!output.isEmpty()) {
                            boolean outSlotAvailable = false;
                            if (outputSlots == null) {
                                outSlotAvailable = true;
                            } else {
                                for (int slot : outputSlots) {
                                    ItemStack stack = te.slots().get(slot);
                                    if (stack.isEmpty() || (ItemHandlerHelper.canItemStacksStack(stack, output) && stack.getCount() + output.getCount() <= te.getMaxStackSize(slot))) {
                                        outSlotAvailable = true;
                                        break;
                                    }
                                }
                            }
                            if (!outSlotAvailable) {
                                return false;
                            }
                        }
                    }
                }
                List<FluidStack> stacks = recipe.fluidOutputs();
                if (stacks != null && !stacks.isEmpty()) {
                    IFluidTank[] tanks = te.tanks();
                    int[] outputTanks = te.outTanks();
                    for (FluidStack output : stacks) {
                        if (output != null && output.getAmount() > 0) {
                            boolean outTankAvail = false;
                            if (tanks == null || outputTanks == null) {
                                outTankAvail = true;
                            } else {
                                for (int i : outputTanks) {
                                    if (i >= 0 && i < outputTanks.length && tanks[i] != null && tanks[i].fill(output, IFluidHandler.FluidAction.SIMULATE) == output.getAmount()) {
                                        outTankAvail = true;
                                        break;
                                    }
                                }
                            }
                            if (!outTankAvail) {
                                return false;
                            }
                        }
                    }
                }
                return te.additionalCheck(this);
            }
            return false;
        }

        public void tick(MultiBlockMachineTileEntity te) {
            int extractedEnergy = energy;
            int frequency = 1;
            if (this.recipe.timeMultiplier() > 1) {
                int insertion = 0;
                int received = te.energyStorage.receiveEnergy(energy, false);
                insertion = (int)Math.round(insertion * 0.5 + received * (1 - 0.5));
                insertion = te.energyStorage.extractEnergy(insertion, true);
                if (insertion > extractedEnergy) {
                    int maxTicksAccordingToEnergy = Math.min(insertion / energy, Math.min(this.recipe.timeMultiplier(), this.maxTime - this.processTime));
                    if (maxTicksAccordingToEnergy > 1) {
                        frequency = maxTicksAccordingToEnergy;
                        extractedEnergy *= frequency;
                    }
                }
            }
            te.energyStorage.extractEnergy(extractedEnergy, false);
            this.processTime += frequency;
            if (this.processTime >= this.maxTime) {
                this.pop(te);
            }
        }

        protected void pop(MultiBlockMachineTileEntity te) {
            List<ItemStack> out = outputs(te);
            if (out != null && !out.isEmpty()) {
                int[] outSlots = te.outSlots();
                for (ItemStack stack : out) {
                    if (!stack.isEmpty()) {
                        if (outSlots == null || te.slots() == null) {
                            te.output(stack.copy());
                        } else {
                            for (int i : outSlots) {
                                ItemStack temp = te.slots().get(i);
                                if (temp.isEmpty()) {
                                    te.slots().set(i, stack.copy());
                                    break;
                                } else if (ItemHandlerHelper.canItemStacksStack(temp, stack) && temp.getCount() + stack.getCount() <= te.getMaxStackSize(i)) {
                                    te.slots().get(i).grow(stack.getCount());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            List<FluidStack> outputs = fluidOutputs(te);
            if (outputs != null && !outputs.isEmpty()) {
                IFluidTank[] tanks = te.tanks();
                int[] outTanks = te.outTanks();
                for (FluidStack output : outputs) {
                    if (output != null && output.getAmount() > 0) {
                        if (tanks == null || outTanks == null) {
                            te.outFluid(output);
                        } else {
                            for (int i : outTanks) {
                                if (i >= 0 && i < tanks.length && tanks[i] != null && tanks[i].fill(output, IFluidHandler.FluidAction.SIMULATE) == output.getAmount()) {
                                    tanks[i].fill(output, IFluidHandler.FluidAction.EXECUTE);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            te.pop(this);
            this.clear = true;
        }

        protected abstract void serializeAdditionalNBT(CompoundTag tag);
    }

    public static class ProcessInside<S extends IMultiBlockRecipe> extends Process<S> {
        protected int[] inSlots = new int[0];
        protected int[] inTanks = new int[0];

        public ProcessInside(S recipe, int... inSlots) {
            super(recipe);
            this.inSlots = inSlots;
        }

        public ProcessInside setInTanks(int... inTanks) {
            this.inTanks = inTanks;
            return this;
        }

        public int[] getInSlots() {
            return inSlots;
        }

        public int[] getInTanks() {
            return inTanks;
        }

        protected List<ItemStack> inputs(MultiBlockMachineTileEntity te) {
            return recipe.itemInputs();
        }

        protected List<FluidStack> fluidInputs(MultiBlockMachineTileEntity te) {
            return recipe.fluidInputs();
        }

        @Override
        public void tick(MultiBlockMachineTileEntity te) {
            List<ItemStack> slots = te.slots();
            if (recipe.itemInputs() != null && slots != null) {
                NonNullList<ItemStack> query = NonNullList.withSize(inSlots.length, ItemStack.EMPTY);
                for (int i = 0; i < inSlots.length; i++) {
                    if (inSlots[i] >= 0 && inSlots[i] < slots.size()) {
                        query.set(i, te.slots().get(inSlots[i]));
                    }
                }
                if (recipe.itemInputs().equals(query)) {
                    this.clear = true;
                    return;
                }
            }
            super.tick(te);
        }

        @Override
        protected void pop(MultiBlockMachineTileEntity te) {
            super.pop(te);
            List<ItemStack> slots = te.slots();
            List<ItemStack> inputs = this.inputs(te);
            if (slots != null && this.inSlots != null && inputs != null) {
                Iterator<ItemStack> it = new ArrayList<>(inputs).iterator();
                while (it.hasNext()) {
                    ItemStack stack = it.next();
                    int stackSize = stack.getCount();
                    for (int i : inSlots) {
                        if (!slots.get(i).isEmpty() && stack.getItem() == slots.get(i).getItem()) {
                            int taken = Math.min(slots.get(i).getCount(), stackSize);
                            slots.get(i).shrink(taken);
                            if (slots.get(i).getCount() <= 0) {
                                slots.set(i, ItemStack.EMPTY);
                            }
                            if ((stackSize -= taken) <= 0) {
                                break;
                            }
                        }
                    }
                }
            }
            IFluidTank[] tanks = te.tanks();
            List<FluidStack> fluidInputs = fluidInputs(te);
            if (tanks != null && this.inTanks != null && fluidInputs != null) {
                Iterator<FluidStack> it = new ArrayList<>(fluidInputs).iterator();
                while (it.hasNext()) {
                    FluidStack stack = it.next();
                    int amount = stack.getAmount();
                    for (int i : inTanks) {
                        if (tanks[i]  != null) {
                            if (tanks[i] instanceof IFluidHandler && ((IFluidHandler)tanks[i]).drain(stack, IFluidHandler.FluidAction.SIMULATE) != null) {
                                FluidStack taken = ((IFluidHandler)tanks[i]).drain(stack, IFluidHandler.FluidAction.EXECUTE);
                                if ((amount -= taken.getAmount()) <= 0) {
                                    break;
                                }
                            } else if (tanks[i].getFluid() != null && tanks[i].getFluid().isFluidEqual(stack)) {
                                int taken = Math.min(tanks[i].getFluidAmount(), amount);
                                tanks[i].drain(taken, IFluidHandler.FluidAction.EXECUTE);
                                if ((amount -= taken) <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        protected void serializeAdditionalNBT(CompoundTag tag) {
            if (inSlots != null) {
                tag.putIntArray("inSlots", inSlots);
            }
            if (inTanks != null) {
                tag.putIntArray("inTanks", inTanks);
            }
        }
    }

    public static class ProcessOutside<H extends IMultiBlockRecipe> extends Process<H> {

        public List<ItemStack> inputs;
        protected float transPoint;

        public ProcessOutside(H recipe, float transPoint, List<ItemStack> inputs) {
            super(recipe);
            this.inputs = inputs;
            this.transPoint = transPoint;
        }

        public List<ItemStack> getDisplayedItem() {
            if (processTime / (float)maxTime > transPoint) {
                List<ItemStack> stacks = this.recipe.itemOutputs();
                if (!stacks.isEmpty()) {
                    return stacks;
                }
            }
            return inputs;
        }

        @Override
        protected void serializeAdditionalNBT(CompoundTag tag) {
            tag.put("inItem", WorldUtils.serializeSlots(inputs));
            tag.putFloat("transPoint", transPoint);
        }

        @Override
        protected void pop(MultiBlockMachineTileEntity te) {
            super.pop(te);
            int size = -1;

            for (ItemStack input : inputs) {
                for (ItemStack stack : recipe.itemInputs()) {
                    if (stack.getItem() == input.getItem()) {
                        size = stack.getCount();
                        break;
                    }
                }
                if (size > 0 && input.getCount() > size) {
                    input.split(size);
                    processTime = 0;
                    clear = false;
                }
            }
        }
    }

    public static class DirectProcessing implements IItemHandlerModifiable {
        MultiBlockMachineTileEntity te;
        float transPoint = 0.5F;
        boolean scheduleProcess = false;

        public DirectProcessing(MultiBlockMachineTileEntity te) {
            this.te = te;
        }

        public DirectProcessing setTransformPoint(float point) {
            this.transPoint = point;
            return this;
        }

        public DirectProcessing setScheduled(boolean scheduled) {
            this.scheduleProcess = scheduled;
            return this;
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int i) {
            return ItemStack.EMPTY;
        }

        @NotNull
        @Override
        public ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
            arg = arg.copy();
            IMultiBlockRecipe recipe = this.te.searchRecipe(arg);
            if (recipe == null) {
                return arg;
            }

            ItemStack display = ItemStack.EMPTY;
            for (ItemStack stack : recipe.itemInputs()) {
                if (stack.equals(arg)) {
                    ItemStack temp = arg.copy();
                    temp.setCount(stack.getCount());
                    break;
                }
            }
            if (te.addTask(new ProcessOutside(recipe, transPoint, new ArrayList<>(Collections.singleton(display))), bl, scheduleProcess)) {
                te.setChanged();
                te.markContainingUpdate(null);
                arg.shrink(display.getCount());
                if (arg.getCount() <= 0) {
                    arg = ItemStack.EMPTY;
                }
            }
            return arg;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int i, int j, boolean bl) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int i) {
            return 64;
        }

        @Override
        public boolean isItemValid(int i, @NotNull ItemStack arg) {
            return true;
        }

        @Override
        public void setStackInSlot(int i, @NotNull ItemStack arg) {

        }
    }
}
