package cc.sukazyo.sericons.block.multiblocks;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.base.StatePropertiesHandler;
import cc.sukazyo.sericons.tile.MetalSmelterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.network.NetworkHooks;
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

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (!world.isClientSide) {
            BlockEntity t = world.getBlockEntity(pos);
            SeriConsMod.LOGGER.info("UseBlock: {} {}", t, pos);
            switch (state.getValue(StatePropertiesHandler.TYPES)) {
                case METAL_SMELTER:
                    NetworkHooks.openGui((ServerPlayer) player, (MetalSmelterTileEntity) t, pos);
                    return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        SeriConsMod.LOGGER.info("Creating TileEntity {}", state);
        switch (state.getValue(StatePropertiesHandler.TYPES)) {
            case METAL_SMELTER:
                return new MetalSmelterTileEntity();
        }
        return null;
    }

    //TODO review
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder builder) {
        Property[] properties = new Property[5];
        properties[0] = StatePropertiesHandler.TYPES;
        properties[1] = StatePropertiesHandler.DYNAMIC_RENDER;
        properties[2] = StatePropertiesHandler.BOOLS[0];
        properties[3] = StatePropertiesHandler.HORIZONTAL_FACING;
        properties[4] = StatePropertiesHandler.SLAVE_MULTIBLOCK;
        builder.add(properties);
    }
}
