package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.multiblock.MultiBlockRegistryHandler;
import cc.sukazyo.sericons.block.BoilerBlock;
import cc.sukazyo.sericons.block.ChalcopyriteBlock;
import cc.sukazyo.sericons.block.CreativeEnergyProviderBlock;
import cc.sukazyo.sericons.block.FeldsparBlock;
import cc.sukazyo.sericons.block.multiblocks.MultiBlockMachine;
import cc.sukazyo.sericons.block.multiblocks.MultiblockMetalSmelter;
import cc.sukazyo.sericons.inventory.MetalSmelterMenu;
import cc.sukazyo.sericons.item.CoarseSiliconItem;
import cc.sukazyo.sericons.item.FeldsparUglyDustItem;
import cc.sukazyo.sericons.loot.ModLootTables;
import cc.sukazyo.sericons.screen.MetalSmelterScreen;
import cc.sukazyo.sericons.tile.BoilerTileEntity;
import cc.sukazyo.sericons.tile.CreativeEnergyProviderTileEntity;
import cc.sukazyo.sericons.tile.MetalSmelterTileEntity;
import cc.sukazyo.sericons.world.OreGen;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        reg.register(new FeldsparBlock().setRegistryName("feldspar"));
        reg.register(new MultiBlockMachine().setRegistryName("multiblock_machine"));
        reg.register(new CreativeEnergyProviderBlock().setRegistryName("creative_energy"));
        reg.register(new ChalcopyriteBlock().setRegistryName("chalcopyrite"));
        reg.register(new BoilerBlock().setRegistryName("boiler"));
    }

    @SubscribeEvent
    public static void registerFluids(final RegistryEvent.Register<Fluid> e) {
        e.getRegistry().registerAll(

        );
    }

    @SubscribeEvent
    public static void registerTileEntities(@Nonnull RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().registerAll(
                BlockEntityType.Builder.of(MetalSmelterTileEntity::new, RegistryBlocks.MULTIBLOCK_MACHINE)
                        .build(null).setRegistryName("metal_smelter"),
                BlockEntityType.Builder.of(CreativeEnergyProviderTileEntity::new, RegistryBlocks.CREATIVE_ENERGY)
                        .build(null).setRegistryName("creative_energy_provider"),
                BlockEntityType.Builder.of(BoilerTileEntity::new, RegistryBlocks.BOILER)
                        .build(null).setRegistryName("boiler")
        );
    }

    @SubscribeEvent
    public static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> reg = event.getRegistry();

        Item.Properties props = new Item.Properties().tab(CREATIVE_TAB);
        reg.register(new BlockItem(RegistryBlocks.FELDSPAR, props).setRegistryName("feldspar"));
        reg.register(new FeldsparUglyDustItem(props).setRegistryName("feldspar_ugly_dust"));
        reg.register(new CoarseSiliconItem().setRegistryName("coarse_silicon"));
        reg.register(new BucketItem(() -> RegistryFluids.steam, new Item.Properties().tab(CREATIVE_TAB).stacksTo(1)).setRegistryName("bucket_steam"));
    }

    @SubscribeEvent
    public static void registerMenus(@Nonnull RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().register(
                IForgeContainerType.create(MetalSmelterMenu::new).setRegistryName("metal_smelter")
        );
    }

    @SubscribeEvent
    public static void gatherData(@Nonnull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new ModLootTables(generator));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeriConsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Client {

        @SubscribeEvent
        public static void setup(FMLClientSetupEvent event) {
            MenuScreens.register(RegistryBlocks.METAL_SMELTER_MENU, MetalSmelterScreen::new);
        }
    }
}
