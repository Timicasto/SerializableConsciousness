package cc.sukazyo.sericons.capabilities;

import net.minecraft.nbt.CompoundTag;

public class DefaultBodyDurabilityCapability implements IBodyDurabilityCapability {
    public double durability;

    public DefaultBodyDurabilityCapability() {
        this.durability = 1.0D;
    }

    @Override
    public double durability() {
        return durability;
    }

    @Override
    public void setDurability(double durability) {
        this.durability = durability;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Durability", this.durability);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        this.durability = arg.getDouble("Durability");
    }
}
