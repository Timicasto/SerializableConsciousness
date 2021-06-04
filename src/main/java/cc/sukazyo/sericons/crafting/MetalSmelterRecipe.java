package cc.sukazyo.sericons.crafting;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MetalSmelterRecipe {
    public static Map<ItemStack, ItemStack> outputs = new HashMap<>();
    public static Map<ItemStack, ItemStack> changers = new HashMap<>();
    public static Map<ItemStack, ItemStack> slags = new HashMap<>();
    public static Map<ItemStack, Integer> time = new HashMap<>();

    public static void register(ItemStack output, ItemStack input, ItemStack slag, ItemStack changer, int processTime) {
        outputs.put(input, output);
        changers.put(input, changer);
        slags.put(input, slag);
        time.put(input, processTime);
    }

    public static ItemStack getResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : outputs.entrySet()) {
            if (input.getItem() == entry.getKey().getItem()) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getSlag(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : slags.entrySet()) {
            if (input.getItem() == entry.getKey().getItem()) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getChanger(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : changers.entrySet()) {
            if (input.getItem() == entry.getKey().getItem()) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    public static int getTime(ItemStack input) {
        for (Map.Entry<ItemStack, Integer> entry : time.entrySet()) {
            if (input.getItem() == entry.getKey().getItem()) {
                return entry.getValue();
            }
        }
        return 2147483647 / 2;
    }
}
