package cc.sukazyo.sericons.world;

import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;

/**
 * Wrapped Ore Generating Property, Including Generating Range, Vein Size and Trying Times.
 */
public class OrePropertiesWrapper {

    public RangeDecoratorConfiguration config;
    public int size;
    public int retryCount;

    /**
     * Default Constructor, All Members Are Public
     *
     * @param config     Generating Range
     * @param size       Vein Size (Max Size)
     * @param retryCount Generate Trying Times (Per Chunk)
     */
    public OrePropertiesWrapper(RangeDecoratorConfiguration config, int size, int retryCount) {
        this.config = config;
        this.size = size;
        this.retryCount = retryCount;
    }
}
