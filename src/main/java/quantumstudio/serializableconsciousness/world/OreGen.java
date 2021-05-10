package quantumstudio.serializableconsciousness.world;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import quantumstudio.serializableconsciousness.ContentHandler;

import java.util.Random;

public class OreGen {
    public static ConfiguredFeature<?, ?> fledspar;

    public static void initFeatures() {
        fledspar = buildFeature(Feature.ORE, ContentHandler.feldsparBlock.get().getDefaultState(), new OrePropertiesWrapper(new TopSolidRangeConfig(12, 0, 35), new Random().nextInt(4) + 2, 2));
        registerFeature(ContentHandler.MODID, "feldspar_vein", fledspar);
        ContentHandler.logger.info("Registered OreGenConf "  + fledspar);
    }

    /**
     * This Method is Used For Generating |ConfiguredFeature|
     * @param feature Feature Type (Ore, Tree, and so on)
     * @param state Generating Block
     * @param config Wrapped Config (Including Size, Range And Chance)
     * @return ConfiguredFeature with defined config
     */
    public static ConfiguredFeature<?, ?> buildFeature(Feature<OreFeatureConfig> feature, BlockState state, OrePropertiesWrapper config) {
        return feature.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, state, config.size)).range(config.config.maximum).square().count(config.retryCount);
    }

    public static void registerFeature(String modid, String key, ConfiguredFeature<?, ?> feature) {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(modid, key), feature);
    }
}
