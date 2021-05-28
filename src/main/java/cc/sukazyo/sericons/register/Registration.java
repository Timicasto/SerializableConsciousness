package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.multiblock.MultiBlockRegistryHandler;
import cc.sukazyo.sericons.block.CreativeEnergyProviderBlock;
import cc.sukazyo.sericons.block.FeldsparBlock;
import cc.sukazyo.sericons.block.multiblocks.MultiBlockMachine;
import cc.sukazyo.sericons.block.multiblocks.MultiblockMetalSmelter;
import cc.sukazyo.sericons.item.FeldsparUglyDustItem;
import cc.sukazyo.sericons.loot.ModLootTables;
import cc.sukazyo.sericons.tile.CreativeEnergyProviderTileEntity;
import cc.sukazyo.sericons.tile.MetalSmelterTileEntity;
import cc.sukazyo.sericons.world.OreGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = SeriConsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Registration {


    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(SeriConsMod.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.PUMPKIN);
        }
    };

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OreGen.initFeatures();
            SeriConsMod.LOGGER.info("OreGenFeatures registered");
        });
        MultiBlockRegistryHandler.registerStructure(MultiblockMetalSmelter.INSTANCE);
        MinecraftForge.EVENT_BUS.addListener(OreGen::onBiomeLoading);
    }

    @SubscribeEvent
    public static void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> reg = event.getRegistry();
        reg.registerAll(
                new FeldsparBlock().setRegistryName("feldspar"),
                new MultiBlockMachine().setRegistryName("multiblock_machine"),
                new CreativeEnergyProviderBlock().setRegistryName("creative_energy")
        );

    }

    @SubscribeEvent
    public static void registerTileEntities(@Nonnull RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().registerAll(
                BlockEntityType.Builder.of(MetalSmelterTileEntity::new, RegistryBlocks.MULTIBLOCK_MACHINE).build(null).setRegistryName("multiblock_machine"),
                BlockEntityType.Builder.of(CreativeEnergyProviderTileEntity::new, RegistryBlocks.CREATIVE_ENERGY).build(null).setRegistryName("creative_energy_provider")
        );
    }

    @SubscribeEvent
    public static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> reg = event.getRegistry();

        Item.Properties props = new Item.Properties().tab(CREATIVE_TAB);
        reg.register(new BlockItem(RegistryBlocks.FELDSPAR, props).setRegistryName("feldspar"));
        reg.register(new FeldsparUglyDustItem(props).setRegistryName("feldspar_ugly_dust"));
    }

    @SubscribeEvent
    public static void gatherData(@Nonnull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new ModLootTables(generator));
        }
    }


}
