package cc.sukazyo.sericons.tile;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.api.energy.EnergyWrapper.CustomEnergyStorage;
import cc.sukazyo.sericons.capabilities.Capabilities;
import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.register.RegistryItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BodyBinderTileEntity extends BlockEntity implements TickableBlockEntity {

    public CustomEnergyStorage storage = new CustomEnergyStorage(8000);

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

        super(RegistryBlocks.BODY_BINDER_BLOCK_ENTITY);
    }

    @Override
    public void tick() {
        if (component.getStackInSlot(0).getCount() >= 8 && storage.getEnergyStored() >= 2000) {
            BlockPos p = getBlockPos();
            List<LivingEntity> list = Objects.requireNonNull(level).getEntitiesOfClass(LivingEntity.class, new AABB(p.getX(), p.getY() - 1F, p.getZ(), p.getX() + 1F, p.getY() + 2F, p.getZ() + 1F));
            if (!list.isEmpty() && list.get(0) instanceof Player) {
                Player tar = (Player) list.get(0);
                if (tar.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().isPresent()) {
                    if (tar.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability() <= 0.5) {
                        tar.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().setDurability(1);
                        component.getStackInSlot(0).shrink(8);
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag serializeNBT() {

        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
