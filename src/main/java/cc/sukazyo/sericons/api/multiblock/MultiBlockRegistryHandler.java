package cc.sukazyo.sericons.api.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockRegistryHandler {
    public static List<IMultiblock> multiblocks = new ArrayList<>();

    public static void registerStructure(IMultiblock structure) {
        multiblocks.add(structure);
    }

    public static List<IMultiblock> getMultiblocks() {
        return multiblocks;
    }

    public interface IMultiblock {

        /**
         * @return name of the structure, This is used for the interdiction NBT system on the build toolbox, so it's must be unique.
         */
        String getUniqueName();

        /**
         * This method is used for checking whether the given block can be used to trigger the structure. <br>
         */
        boolean isTrigger(BlockState state);

        /**
         * This method checks whether the structure is valid and try to create a new formed structure here.
         * @return if the structure was valid and transformed
         */
        boolean createStructure(Level level, BlockPos pos, Direction direction, Player player);

        /**
         * @return A three-dimensional array (x, y, z) of the structure.
         */
        BlockState[][][] getStructure();

        /**
         * Use this to overwrite the rendering of a structure's component
         */
        @OnlyIn(Dist.CLIENT)
        boolean overwriteRender(ItemStack stack, int iterator);

        /**
         * Returns the scale modifier to be applied when rendering in the manual
         */
        float getManualScale();

        /**
         * Returns true to add a button that can switch the assembly
         */
        @OnlyIn(Dist.CLIENT)
        boolean canRenderFormedStructure();

        /**
         * Use this method to render the completed structure
         */
        @OnlyIn(Dist.CLIENT)
        void renderFormedStructure();
    }

    /**
     * This event is fired BEFORE THE STRUCTURE IS ATTEMPTED TO BE FORMED. <br>
     * NO CHECKS of the structure have been made. The event simply exists to cancel the formation of the structure.
     */
    @Cancelable
    public static class MultiBlockFormEvent extends PlayerEvent {
        public final IMultiblock structure;
        public final BlockPos clickedPos;
        public final ItemStack toolbox;

        public MultiBlockFormEvent(Player player, IMultiblock structure, BlockPos clickedPos, ItemStack toolbox) {
            super(player);
            this.structure = structure;
            this.clickedPos = clickedPos;
            this.toolbox = toolbox;
        }
    }
}








