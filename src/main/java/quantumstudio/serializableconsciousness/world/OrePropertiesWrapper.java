package quantumstudio.serializableconsciousness.world;

import net.minecraft.world.gen.placement.TopSolidRangeConfig;

/**
 * Wrapped Ore Generating Property, Including Generating Range, Vein Size and Trying Times.
 */
public class OrePropertiesWrapper {
    public TopSolidRangeConfig config;
    public int size;
    public int retryCount;

    /**
     * Default Constructor, All Members Are Public
     * @param config Generating Range
     * @param size Vein Size (Max Size)
     * @param retryCount Generate Trying Times (Per Chunk)
     */
    public OrePropertiesWrapper(TopSolidRangeConfig config, int size, int retryCount) {
        this.config = config;
        this.size = size;
        this.retryCount = retryCount;
    }
}
