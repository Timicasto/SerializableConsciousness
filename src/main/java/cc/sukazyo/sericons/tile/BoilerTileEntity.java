package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.register.RegistryFluids;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoilerTileEntity extends BlockEntity implements TickableBlockEntity {


    public int fuel, water, gas;
    public final IFluidHandler tank = new FluidTank(8000), steam = new FluidTank(40000);
    public int temp = 30;

    private boolean running, isTempIncreasing, output;

    public BoilerTileEntity() {
        super(RegistryBlocks.BOILER_TILE_ENTITY);
    }


    public void insertFuel() {
        fuel += 3200;
    }



    public void start() {
        if (!running) {
            running = SeriConsMod.RANDOM.nextBoolean();
        }
    }

    @Override
    public void tick() {
        if (running) {
            if (isTempIncreasing) {
                if (temp < 100) {
                    output = false;
                    ++temp;
                } else {
                    isTempIncreasing = false;
                }
            } else {
                if (temp < 100) {
                    isTempIncreasing = true;
                } else {
                    output = true;
                }
            }

            if (output && tank.getFluidInTank(0).getAmount() > 0) {
                tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                steam.fill(new FluidStack(RegistryFluids.steam, 1), IFluidHandler.FluidAction.EXECUTE);
            }
        }
        System.out.println("Temperature: " + temp);
        System.out.println("Water: " + tank.getFluidInTank(0).getDisplayName() + " * " + tank.getFluidInTank(0).getAmount());
        System.out.println("Gas: " + steam.getFluidInTank(0).getDisplayName() + " * " + steam.getFluidInTank(0).getAmount());
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T)tank);
        }
        return LazyOptional.empty();
    }
}
