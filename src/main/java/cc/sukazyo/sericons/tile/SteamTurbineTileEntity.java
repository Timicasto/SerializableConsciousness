package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.kinetic.KineticStorage;
import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.util.PerlinNoiseGenerator;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class SteamTurbineTileEntity extends BlockEntity implements TickableBlockEntity {

    public KineticStorage storage = new KineticStorage();
    public IFluidHandler handler = new FluidTank(200000);
    public PerlinNoiseGenerator generator = new PerlinNoiseGenerator();
    public boolean running;
    public int i;

    public SteamTurbineTileEntity() {
        super(RegistryBlocks.STEAM_TURBINE_TILE_ENTITY);
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void tick() {
        if (level.getBlockEntity(getBlockPos().east()) instanceof BoilerTileEntity) {
            BoilerTileEntity te = (BoilerTileEntity) level.getBlockEntity(getBlockPos().east());
            FluidStack steam = te.steam.getFluidInTank(0);
            handler.fill(steam, IFluidHandler.FluidAction.EXECUTE);
            te.steam.drain(steam, IFluidHandler.FluidAction.EXECUTE);
        }
        if (isRunning()) {
            if (handler.getFluidInTank(0).getAmount() > 0) {
                running = true;
                i = 0;
            }
        } else {
            if (handler.getFluidInTank(0).getAmount() > 0) {
                ++i;
                storage.setkCurrent(generator.generateHeight(i, i) + 50);
                handler.drain(1, IFluidHandler.FluidAction.EXECUTE);
            } else {
                running = false;
                i = 0;
            }
        }
    }
}
