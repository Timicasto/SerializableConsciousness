package cc.sukazyo.sericons.api.utils;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorldUtils {

    public static ListTag serializeSlots(Collection<ItemStack> slots) {
        ListTag tags = new ListTag();
        byte slot = 0;
        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) {
                CompoundTag item = new CompoundTag();
                item.putByte("slot", slot);
                stack.save(item);
                tags.add(item);
            }
            slot++;
        }
        return tags;
    }

    /*public static List<ItemStack> deserialize(Tag nbt) {
        List<ItemStack> stacks = new ArrayList<>();
        if (nbt instanceof CompoundTag) {
            ItemStack stack = ItemStack.of((CompoundTag)nbt);
            stacks.add(stack);
            return stacks;
        } else if (nbt instanceof ListTag) {
            ListTag tag = (ListTag)nbt;
            return
        }
    }*/

}
