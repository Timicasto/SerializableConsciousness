package cc.sukazyo.sericons.world;

/**
 * Wrapped Ore Generating Property, Including Generating Range, Vein Size and Trying Times.
 */
public class OreConfig {

    public final int bottomOffset;
    public final int topOffset;
    public final int maximum;
    public final int maxSize;
    public final int perChunk;

    /**
     * Ore config, generation height is between {@code [bottomOffset, maximum - topOffset + bottomOffset)}
     *
     * @param maxSize  max vein size, [0, 64]
     * @param perChunk generation count per chunk
     */
    public OreConfig(int bottomOffset, int topOffset, int maximum, int maxSize, int perChunk) {
        this.bottomOffset = bottomOffset;
        this.topOffset = topOffset;
        this.maximum = maximum;
        this.maxSize = maxSize;
        this.perChunk = perChunk;
    }

    /**
     * Ore config, generation height is between [minHeight, maxHeight)
     */
    public OreConfig(int minHeight, int maxHeight, int maxSize, int perChunk) {
        this.bottomOffset = minHeight;
        this.topOffset = minHeight;
        this.maximum = maxHeight;
        this.maxSize = maxSize;
        this.perChunk = perChunk;
    }
}
