package cc.sukazyo.sericons.api.multiblock.dynstrctures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public class Element {
    public Block block;
    public BlockPos pos;
    public IDynamicNetwork parent;

    public Element(Block block, BlockPos pos, IDynamicNetwork parent) {
        this.block = block;
        this.pos = pos;
        this.parent = parent;
    }
}
