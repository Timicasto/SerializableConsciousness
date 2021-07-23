package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.register.RegistryItems;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BodyBinderTileEntity extends BlockEntity implements TickableBlockEntity {
    public ItemStackHandler component = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() == RegistryItems.BIONIC_BODY_COMPONENT;
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 32;
        }
    };

    public BodyBinderTileEntity() {
        super(RegistryBlocks.BOILER_TILE_ENTITY);
    }

    @Override
    public void tick() {

    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
