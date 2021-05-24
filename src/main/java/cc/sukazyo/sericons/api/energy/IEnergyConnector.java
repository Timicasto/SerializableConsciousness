package cc.sukazyo.sericons.api.energy;

import net.minecraft.core.Direction;

import javax.annotation.Nonnull;

public interface IEnergyConnector extends IEnergyConnection {
    @Nonnull
    EnumModeProperty getSideMode(Direction facing);

    @Override
    default boolean canDoIO(Direction facing) {
        return getSideMode(facing) != EnumModeProperty.BLOCK;
    }

    EnergyWrapper.EnergyWrapperImpl getWrappedCapability(Direction facing);

    default void notifyTransferUpdate(int energy, boolean simulate) {

    }
}
