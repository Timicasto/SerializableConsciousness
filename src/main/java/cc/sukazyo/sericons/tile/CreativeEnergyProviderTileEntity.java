package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.api.energy.EnergyWrapper;
import cc.sukazyo.sericons.register.RegistryBlocks;
import com.google.common.collect.Queues;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Queue;

public class CreativeEnergyProviderTileEntity extends BlockEntity implements TickableBlockEntity {

    public EnergyWrapper.CustomEnergyStorage storage;
    public boolean b = true;
    int energy;

    public CreativeEnergyProviderTileEntity() {
        super(RegistryBlocks.CREATIVE_ENERGY_PROVIDER);
    }

    @Override
    public void tick() {
        if (b) {
            energy = storage.getEnergyStored();
            b = false;
        }

        if (this.level != null && !this.level.isClientSide) {
            this.output();
            this.transfer(this.level);
        }
    }

    private void output() {
        energy += 4000;
        this.storage.setEnergyStored(energy);
    }

    public final Queue<Direction> directions = Queues.newArrayDeque(Direction.Plane.HORIZONTAL);

    private void transfer(@Nonnull Level world) {
        this.directions.offer(this.directions.remove());
        for (Direction direction : this.directions) {
            BlockEntity te = world.getBlockEntity(this.worldPosition.relative(direction));
            if (te != null) {
                te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(e -> {
                    if (e.canReceive()) {
                        int diff = e.receiveEnergy(Math.min(500, this.energy), false);
                        if (diff != 0) {
                            this.energy -= diff;
                            this.setChanged();
                        }
                    }
                });
            }
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag ret = new CompoundTag();
        ret.putInt("Energy", storage.getEnergyStored());
        return ret;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storage.setEnergyStored(nbt.getInt("Energy"));
        super.deserializeNBT(nbt);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.of(() -> (T)storage);
    }
}
