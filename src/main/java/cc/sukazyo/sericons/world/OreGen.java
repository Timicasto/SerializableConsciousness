package cc.sukazyo.sericons.world;

import cc.sukazyo.sericons.SeriCons;
import cc.sukazyo.sericons.register.RegistryBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.ModLoader;

import javax.annotation.Nonnull;

public class OreGen {

    private static ConfiguredFeature<?, ?> FELDSPAR;

    public static void initFeatures() {
        if (ModLoader.isLoadingStateValid()) {
            FELDSPAR = getOreFeature(Feature.ORE, RegistryBlocks.FELDSPAR, "feldspar_vein",
                    new OreConfig(12, 0, 35, 20, 8));
        }
    }

    public static void onBiomeLoading(@Nonnull BiomeLoadingEvent event) {
        Biome.BiomeCategory category = event.getCategory();
        if (category != Biome.BiomeCategory.THEEND &&
                category != Biome.BiomeCategory.NETHER &&
                category != Biome.BiomeCategory.NONE) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, FELDSPAR);
        }
    }

    /**
     * This Method is Used For Generating |ConfiguredFeature|
     *
     * @param feature Feature Type (Ore, Tree, and so on)
     * @param block   Generating Block
     * @param config  Wrapped Config (Including Size, Range And Chance)
     * @return ConfiguredFeature with defined config
     */
    @SuppressWarnings("SameParameterValue")
    private static ConfiguredFeature<?, ?> getOreFeature(Feature<OreConfiguration> feature, Block block,
                                                         String key, OreConfig config) {
        OreConfiguration configuration = new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE,
                block.defaultBlockState(), config.maxSize);
        ConfiguredFeature<?, ?> configured = feature.configured(configuration)
                .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(
                        config.bottomOffset, config.topOffset, config.maximum)))
                .squared()
                .count(config.perChunk);
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SeriCons.MODID, key), configured);
    }
}
