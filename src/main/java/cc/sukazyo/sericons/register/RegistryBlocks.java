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
    public static final Block CHALCOPYRITE = Blocks.AIR;
    public static final Block BOILER = Blocks.AIR;
    public static final Block STEAM_TURBINE = Blocks.AIR;
    public static final Block GENERATOR = Blocks.AIR;

    public static final Block MULTIBLOCK_MACHINE = Blocks.AIR;

    public static final BlockEntityType<?> METAL_SMELTER = BlockEntityType.FURNACE;
    public static final BlockEntityType<?> CREATIVE_ENERGY_PROVIDER = BlockEntityType.FURNACE;
    @ObjectHolder("boiler")
    public static final BlockEntityType<?> BOILER_TILE_ENTITY = BlockEntityType.FURNACE;

    @ObjectHolder("metal_smelter")
    public static final MenuType<MetalSmelterMenu> METAL_SMELTER_MENU = null;

    @ObjectHolder("steam_turbine")
    public static final BlockEntityType<?> STEAM_TURBINE_TILE_ENTITY = BlockEntityType.FURNACE;

    @ObjectHolder("generator")
    public static final BlockEntityType<?> GENERATOR_TILE_ENTITY = BlockEntityType.FURNACE;

}
