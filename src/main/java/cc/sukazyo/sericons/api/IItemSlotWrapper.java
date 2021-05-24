package cc.sukazyo.sericons.api;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * An Interface Implemented By TileEntities That Have Item Slots
 */
public interface IItemSlotWrapper {
    List<ItemStack> slots();
    int getMaxStackSize(int slot);
    void render(int slot);
    boolean canInsert(int slot, ItemStack tar);

    default List<ItemStack> getDrops() {
        return slots();
    }
}
