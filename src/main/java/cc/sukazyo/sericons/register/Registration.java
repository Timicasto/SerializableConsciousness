package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.multiblock.MultiBlockRegistryHandler;
import cc.sukazyo.sericons.block.*;
import cc.sukazyo.sericons.block.multiblocks.MultiBlockMachine;
import cc.sukazyo.sericons.block.multiblocks.MultiblockMetalSmelter;
import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.fluid.SteamFluid;
import cc.sukazyo.sericons.inventory.MetalSmelterMenu;
import cc.sukazyo.sericons.item.BionicBodyComponentItem;
import cc.sukazyo.sericons.item.FeldsparUglyDustItem;
import cc.sukazyo.sericons.loot.ModLootTables;
import cc.sukazyo.sericons.network.NetworkChannel;
import cc.sukazyo.sericons.screen.MetalSmelterScreen;
import cc.sukazyo.sericons.tile.*;
import cc.sukazyo.sericons.world.OreGen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = SeriConsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Registration {

    public static final ResourceLocation still = new ResourceLocation(SeriConsMod.MODID, "block/steam_still");
    public static final ResourceLocation flowing = new ResourceLocation(SeriConsMod.MODID, "block/steam_flowing");

    @SubscribeEvent
    public static void onSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkChannel::regPacket);
        MetalSmelterRecipe.register(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(Items.GLOWSTONE_DUST, 1), new ItemStack(Items.COAL, 1), 600);
    }

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(SeriConsMod.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegistryBlocks.FELDSPAR);
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
        reg.register(new SteamTurbineBlock().setRegistryName("steam_turbine"));
        reg.register(new GeneratorBlock().setRegistryName("generator"));
        reg.register(new AncientRemainBlock().setRegistryName("ancient_remain"));
        reg.register(new BodyBinderBlock().setRegistryName("body_binder"));
        reg.register(new LiquidBlock(() -> RegistryFluids.STEAM_FLUID, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100F).noDrops()).setRegistryName("steam_block"));
    }

    @SubscribeEvent
    public static void registerFluids(final RegistryEvent.Register<Fluid> e) {
        ForgeFlowingFluid.Properties p = new ForgeFlowingFluid.Properties(() -> RegistryFluids.STEAM_FLUID, () -> RegistryFluids.STEAM_FLUID_FLOWING, FluidAttributes.builder(still, flowing).color(0xFFCFCFF).density(400).viscosity(400)).bucket(() -> RegistryItems.BUCKET_STEAM).block(() -> RegistryBlocks.STEAM_BLOCK).slopeFindDistance(3).explosionResistance(100);
        e.getRegistry().register(new ForgeFlowingFluid.Source(p).setRegistryName("steam_fluid"));
        e.getRegistry().register(new ForgeFlowingFluid.Flowing(p).setRegistryName("steam_fluid_flowing"));
    }

    @SubscribeEvent
    public static void registerTileEntities(@Nonnull RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().registerAll(
                BlockEntityType.Builder.of(MetalSmelterTileEntity::new, RegistryBlocks.MULTIBLOCK_MACHINE)
                        .build(null).setRegistryName("metal_smelter"),
                BlockEntityType.Builder.of(CreativeEnergyProviderTileEntity::new, RegistryBlocks.CREATIVE_ENERGY)
                        .build(null).setRegistryName("creative_energy_provider"),
                BlockEntityType.Builder.of(BoilerTileEntity::new, RegistryBlocks.BOILER)
                        .build(null).setRegistryName("boiler"),
                BlockEntityType.Builder.of(SteamTurbineTileEntity::new, RegistryBlocks.STEAM_TURBINE)
                        .build(null).setRegistryName("steam_turbine"),
                BlockEntityType.Builder.of(GeneratorTileEntity::new, RegistryBlocks.GENERATOR)
                        .build(null).setRegistryName("generator"),
                BlockEntityType.Builder.of(BodyBinderTileEntity::new, RegistryBlocks.BODY_BINDER)
                        .build(null).setRegistryName("body_binder")
        );
    }

    @SubscribeEvent
    public static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> reg = event.getRegistry();

        Item.Properties props = new Item.Properties().tab(CREATIVE_TAB);
        reg.register(new BlockItem(RegistryBlocks.FELDSPAR, props).setRegistryName("feldspar"));
        reg.register(new BlockItem(RegistryBlocks.CHALCOPYRITE, props).setRegistryName("chalcopyrite"));
        reg.register(new BlockItem(RegistryBlocks.STEAM_TURBINE, props).setRegistryName("steam_turbine"));
        reg.register(new BlockItem(RegistryBlocks.BOILER, props).setRegistryName("boiler"));
        reg.register(new BlockItem(RegistryBlocks.ANCIENT_REMAIN, props).setRegistryName("ancient_remain"));
        reg.register(new BlockItem(RegistryBlocks.BODY_BINDER, props).setRegistryName("body_binder"));
        reg.register(new BlockItem(RegistryBlocks.GENERATOR, props).setRegistryName("generator"));
        reg.register(new FeldsparUglyDustItem(props).setRegistryName("feldspar_ugly_dust"));
        reg.register(new BucketItem(() -> RegistryFluids.STEAM_FLUID, props.stacksTo(1)).setRegistryName("bucket_steam"));
        reg.register(new BionicBodyComponentItem().setRegistryName("bionic_body_component"));
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
