package quantumstudio.serializableconsciousness.group;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModItemGroup extends ItemGroup {
    public ModItemGroup() {
        super("serializable_consciousness_group");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.PUMPKIN);
    }
}
