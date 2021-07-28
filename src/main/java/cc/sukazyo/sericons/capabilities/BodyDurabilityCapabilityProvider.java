package cc.sukazyo.sericons.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BodyDurabilityCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private IBodyDurabilityCapability cap;

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == Capabilities.BODY_DURATION_CAPABILITY ? LazyOptional.of(this::getOrCreate).cast() : LazyOptional.empty();
    }

    @NotNull
    IBodyDurabilityCapability getOrCreate() {
        if (cap == null) {
            cap = new DefaultBodyDurabilityCapability();
        }
        return this.cap;
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreate().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        getOrCreate().deserializeNBT(arg);
    }
}
