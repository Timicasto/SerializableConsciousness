package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.block.multiblocks.MultiBlockMachine;
import cc.sukazyo.sericons.tile.MultiBlockPartTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holds registered blocks, block entity types.
 */
@ObjectHolder(SeriConsMod.MODID)
public class RegistryBlocks {

    public static final Block FELDSPAR = Blocks.AIR;

    public static final MultiBlockMachine MULTIBLOCK_MACHINE = (MultiBlockMachine) new MultiBlockMachine();

}
