package cc.sukazyo.sericons.world;

import cc.sukazyo.sericons.ContentHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;

import java.util.Random;

public class OreGen {
    public static ConfiguredFeature<?, ?> fledspar;

    public static void initFeatures() {
        fledspar = buildFeature(Feature.ORE, ContentHandler.feldsparBlock.get().defaultBlockState(),
                new OrePropertiesWrapper(new RangeDecoratorConfiguration(12, 0, 35), new Random().nextInt(4) + 2, 2));
        registerFeature(ContentHandler.MODID, "feldspar_vein", fledspar);
        ContentHandler.logger.info("Registered OreGenConf " + fledspar);
    }

    /**
     * This Method is Used For Generating |ConfiguredFeature|
     *
     * @param feature Feature Type (Ore, Tree, and so on)
     * @param state   Generating Block
     * @param config  Wrapped Config (Including Size, Range And Chance)
     * @return ConfiguredFeature with defined config
     */
    public static ConfiguredFeature<?, ?> buildFeature(Feature<OreConfiguration> feature, BlockState state, OrePropertiesWrapper config) {
        return feature.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, state, config.size))
                .range(config.config.maximum).squared().count(config.retryCount);
    }

    @Deprecated
    public static void registerFeature(String modid, String key, ConfiguredFeature<?, ?> feature) {
        //Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(modid, key), feature);
    }
}
