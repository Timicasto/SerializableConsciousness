package cc.sukazyo.sericons.inventory;

import cc.sukazyo.sericons.block.multiblocks.MultiBlockMachine;
import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.tile.MetalSmelterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class MetalSmelterMenu extends AbstractContainerMenu {

    private final MetalSmelterTileEntity smelter;
    private MetalSmelterTileEntity.MetalSmelterItemIntArray array;


    // client
    public MetalSmelterMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        super(RegistryBlocks.METAL_SMELTER_MENU, containerId);
        BlockEntity t = inv.player.level.getBlockEntity(buf.readBlockPos());
        smelter = t instanceof MetalSmelterTileEntity ? (MetalSmelterTileEntity) t : null;
        Container container = new SimpleContainer(smelter.slots.toArray(new ItemStack[]{}));
        this.addSlot(new Slot(container, 0, 20, 32));
        this.addSlot(new Slot(container, 1, 60, 32));
        this.addSlot(new Slot(container, 2, 20, 60));
        this.addSlot(new Slot(container, 3, 60, 60));
        layoutPlayerInventorySlots(inv, 8, 84);
    }

    // server
    public MetalSmelterMenu(int containerId, Inventory inv, Container container, MetalSmelterTileEntity tile, MetalSmelterTileEntity.MetalSmelterItemIntArray array) {
        super(RegistryBlocks.METAL_SMELTER_MENU, containerId);
        this.array = array;
        addDataSlots(array);
        smelter = tile;
        this.addSlot(new Slot(container, 0, 20, 32));
        this.addSlot(new Slot(container, 1, 60, 32));
        this.addSlot(new Slot(container, 2, 20, 60));
        this.addSlot(new Slot(container, 3, 60, 60));
        layoutPlayerInventorySlots(inv, 8, 84);
    }

    // server-side logic
    @Override
    public boolean stillValid(Player player) {
        if (smelter != null) {
            BlockPos pos = smelter.getBlockPos();
            return player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= 64;
        }
        return false;
    }


    private int addSlotRange(Inventory inventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(inventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Inventory inventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(inventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Inventory inventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(inventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(inventory, 0, leftCol, topRow, 9, 18);
    }
}
