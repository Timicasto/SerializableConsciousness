package cc.sukazyo.sericons.api.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataUtils {

    public static List<ItemStack> readSlots(ListTag tag, int size) {
        List<ItemStack> ret = new ArrayList<>();
        int count = tag.size();
        for (int i = 0; i < count; i++) {
            CompoundTag compound = tag.getCompound(i);
            int slot = compound.getByte("Slot") & 255;
            if (slot < size) {
                ret.set(slot, ItemStack.of(compound));
            }
        }
        return ret;
    }

    public static ListTag writeSlots(ItemStack[] stacks) {
        ListTag ret = new ListTag();
        for (int i = 0; i < stacks.length; i++) {
            if (!stacks[i].isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte)i);
                tag = stacks[i].serializeNBT();
                ret.add(tag);
            }
        }
        return ret;
    }

    public static ListTag writeSlots(Collection<ItemStack> slots) {
        ListTag ret = new ListTag();
        byte slot = 0;
        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", slot);
                tag = stack.serializeNBT();
                ret.add(tag);
            }
            slot++;
        }
        return ret;
    }
}
