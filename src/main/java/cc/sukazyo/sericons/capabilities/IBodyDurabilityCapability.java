package cc.sukazyo.sericons.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBodyDurabilityCapability extends INBTSerializable<CompoundTag> {
    double durability();
    void setDurability(double durability);
}
