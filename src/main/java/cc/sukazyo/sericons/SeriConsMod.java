package cc.sukazyo.sericons;

import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SeriConsMod.MODID)
public class SeriConsMod {

    public static final String MODID = "serializableconsciousness";

    public static final Logger LOGGER = LogManager.getLogger("SeriCons");

    public SeriConsMod() {
        MetalSmelterRecipe.register(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(Items.GLOWSTONE_DUST, 1), new ItemStack(Items.COAL, 1), 1200);

    }
}
