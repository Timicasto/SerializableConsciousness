package cc.sukazyo.sericons.api.multiblock;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public interface IStateBlock {
    String name();
    Property property();
    Enum[] enums();
    BlockState state(int i);
    boolean customMapper();
    String customMapping(int i);
    boolean appendProperty();
}
