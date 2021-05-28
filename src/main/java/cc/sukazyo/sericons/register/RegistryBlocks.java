package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.inventory.MetalSmelterMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holds registered blocks, block entity types and their menu types.
 */
@ObjectHolder(SeriConsMod.MODID)
public class RegistryBlocks {

    public static final Block FELDSPAR = Blocks.AIR;
    public static final Block CREATIVE_ENERGY = Blocks.AIR;

    public static final Block MULTIBLOCK_MACHINE = Blocks.AIR;

    public static final BlockEntityType<?> METAL_SMELTER = BlockEntityType.FURNACE;
    public static final BlockEntityType<?> CREATIVE_ENERGY_PROVIDER = BlockEntityType.FURNACE;

    @ObjectHolder("metal_smelter")
    public static final MenuType<MetalSmelterMenu> METAL_SMELTER_MENU = null;
}
