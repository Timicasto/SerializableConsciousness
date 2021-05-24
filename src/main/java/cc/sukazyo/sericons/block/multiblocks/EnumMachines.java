package cc.sukazyo.sericons.block.multiblocks;

import cc.sukazyo.sericons.api.multiblock.IEnumPropertyBlock;
import net.minecraft.util.StringRepresentable;

public enum EnumMachines implements IEnumPropertyBlock, StringRepresentable {
    METAL_SMELTER(false),
    BUFFER(false);

    public boolean customState;

    EnumMachines(boolean customState) {
        this.customState = customState;
    }

    @Override
    public String getSerializedName() {
        return this.toString().toLowerCase();
    }


    @Override
    public boolean displayInItemGroup() {
        return false;
    }

    public boolean isCustomState() {
        return customState;
    }

    public String getCustomState() {
        return getSerializedName().toLowerCase();
    }
}
