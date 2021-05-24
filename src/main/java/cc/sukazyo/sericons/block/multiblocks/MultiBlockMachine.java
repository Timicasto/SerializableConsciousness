package cc.sukazyo.sericons.block.multiblocks;

import cc.sukazyo.sericons.api.base.StatePropertiesHandler;
import cc.sukazyo.sericons.tile.MetalSmelterTileEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class MultiBlockMachine extends MultiBlockTemplateBlock<EnumMachines> {
    public MultiBlockMachine() {
        super("machine", Material.METAL, StatePropertiesHandler.TYPES, StatePropertiesHandler.DYNAMIC_RENDER, StatePropertiesHandler.BOOLS[0]);
    }

    @Override
    public boolean customMapper() {
        return true;
    }

    @Override
    public String customMapping(int i) {
        if (EnumMachines.values()[i].customState) {
            return EnumMachines.values()[i].getCustomState();
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        switch (state.getValue(StatePropertiesHandler.TYPES)) {
            case METAL_SMELTER:
                return new MetalSmelterTileEntity();
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder builder) {
        Property[] properties = new Property[5];
        properties[0] = StatePropertiesHandler.TYPES;
        properties[1] = StatePropertiesHandler.DYNAMIC_RENDER;
        properties[2] = StatePropertiesHandler.BOOLS[0];
        properties[3] = StatePropertiesHandler.HORIZONTAL_FACING ;
        properties[4] = StatePropertiesHandler.SLAVE_MULTIBLOCK;
        builder.add(properties);
    }
}
