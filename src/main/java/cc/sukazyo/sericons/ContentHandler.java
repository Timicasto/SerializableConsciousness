package cc.sukazyo.sericons;

import cc.sukazyo.sericons.block.FeldsparBlock;
import cc.sukazyo.sericons.group.ModItemGroup;
import cc.sukazyo.sericons.item.FeldsparUglyDustItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentHandler {

    public static final String MODID = "serializableconsciousness";
    public static final String NAME = "Serializable Consciousness";

    public static Logger logger = LogManager.getLogger();

    public static final CreativeModeTab CREATIVE_TAB = new ModItemGroup();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> feldsparBlock = BLOCKS.register("feldspar", FeldsparBlock::new);


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> feldsparItemBlock = ITEMS.register("feldspar", () -> new BlockItem(feldsparBlock.get(), new Item.Properties().tab(CREATIVE_TAB)));
    public static final RegistryObject<Item> feldsparUglyDustItem = ITEMS.register("feldspar_ugly_dust", FeldsparUglyDustItem::new);
}
