package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.api.multiblock.MultiBlockRegistryHandler;
import cc.sukazyo.sericons.block.multiblocks.MultiblockMetalSmelter;
import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.register.Registration;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetalSmelterTileEntity extends MultiBlockMachineTileEntity<MetalSmelterTileEntity, MetalSmelterRecipe>{
    public MetalSmelterTileEntity() {
        super(new int[] {5, 6, 5}, Registration.metalSmelterTE.get(), 32000, true, MultiblockMetalSmelter.INSTANCE);
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
        return false;
    }

    @Override
    public void output(ItemStack out) {

    }

    @Override
    public void outFluid(FluidStack out) {

    }

    @Override
    public void pop(Process<MetalSmelterRecipe> process) {

    }

    @Override
    public int maxProcessPerTick() {
        return 0;
    }

    @Override
    public int maxQueueLength() {
        return 0;
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
        return null;
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
}
