package cc.sukazyo.sericons.api.multiblock.dynstrctures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IDynamicNetwork {
    List<List<List<Element>>> getElements();
    Vec3i minSize();
    Vec3i maxSize();

    boolean check();

    void scan(Level world, BlockPos pos);
}
