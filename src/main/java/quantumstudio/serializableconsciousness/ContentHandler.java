package quantumstudio.serializableconsciousness;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quantumstudio.serializableconsciousness.block.FeldsparBlock;
import quantumstudio.serializableconsciousness.group.ModItemGroup;
import quantumstudio.serializableconsciousness.item.FeldsparUglyDustItem;

public class ContentHandler {
    public static final String MODID = "serializableconsciousness";
    public static final String NAME = "Serializable Consciousness";
    public static final String VERSION = "Dev 0.0.1";

    public static Logger logger = LogManager.getLogger();

    public static final ItemGroup modGroup = new ModItemGroup();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> feldsparBlock = BLOCKS.register("feldspar", FeldsparBlock::new);


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> feldsparItemBlock = ITEMS.register("feldspar", () -> new BlockItem(feldsparBlock.get(), new Item.Properties().group(modGroup)));
    public static final RegistryObject<Item> feldsparUglyDustItem = ITEMS.register("feldspar_ugly_dust", FeldsparUglyDustItem::new);
}
