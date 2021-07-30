package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.kinetic.KineticStorage;
import cc.sukazyo.sericons.register.RegistryBlocks;
import com.google.common.collect.Queues;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

public class GeneratorTileEntity extends BlockEntity implements TickableBlockEntity {
    public EnergyWrapper.CustomEnergyStorage storage = new EnergyWrapper.CustomEnergyStorage(40000);
    public KineticStorage kinetic = new KineticStorage();

    public GeneratorTileEntity() {
        super(RegistryBlocks.GENERATOR_TILE_ENTITY);
    }

    Queue<Direction> directions = Queues.newArrayDeque(Direction.Plane.HORIZONTAL);

    public void findValidTurbine(Level level) {
        directions.offer(directions.remove());
        directions.forEach(e -> {
            BlockEntity te = level.getBlockEntity(worldPosition.relative(e));
            if (te instanceof SteamTurbineTileEntity) {
                kinetic.setkCurrent(((SteamTurbineTileEntity) te).storage.getkCurrent());
            }
        });
    }

    @Override
    public void tick() {
        findValidTurbine(level);
        storage.receiveEnergy((int) (kinetic.kCurrent / 5), false);
        transferEnergy(level);
    }

    private final Queue<Direction> queue = Queues.newArrayDeque(Direction.Plane.HORIZONTAL);

    private void transferEnergy(@NotNull Level level) {
        this.queue.offer(this.queue.remove());
        for (Direction direction : queue) {
            BlockEntity te = level.getBlockEntity(this.getBlockPos().relative(direction));
            if (te != null) {
                te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(e -> {
                    if (e.canReceive()) {
                        int diff = e.receiveEnergy(Math.min(500, this.storage.getEnergyStored()), false);
                        if (diff != 0) {
                            this.storage.extractEnergy(this.storage.getEnergyStored() - diff, false);
                            this.setChanged();
                        }
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> (T)storage);
        }
        return LazyOptional.empty();
    }
}
