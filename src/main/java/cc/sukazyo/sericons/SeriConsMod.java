package cc.sukazyo.sericons;

import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.register.RegistryItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(SeriConsMod.MODID)
public class SeriConsMod {

    public static final String MODID = "serializableconsciousness";

    public static final Logger LOGGER = LogManager.getLogger("SeriCons");

    public static final Random RANDOM = new Random();

    public SeriConsMod() {
        MetalSmelterRecipe.register(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(Items.GLOWSTONE_DUST, 1), new ItemStack(Items.COAL, 1), 1200);
        MetalSmelterRecipe.register(new ItemStack(RegistryItems.COARSE_SILICON, 2), new ItemStack(Blocks.SAND, 1), new ItemStack(Items.BONE), new ItemStack(Items.COAL, 4), 6000);

    }
}
