package cc.sukazyo.sericons.group;

import cc.sukazyo.sericons.ContentHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModItemGroup extends CreativeModeTab {

    public ModItemGroup() {
        super(ContentHandler.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.PUMPKIN);
    }
}
