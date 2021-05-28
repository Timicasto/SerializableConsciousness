package cc.sukazyo.sericons.inventory;

import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.tile.MetalSmelterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MetalSmelterMenu extends AbstractContainerMenu {

    private final MetalSmelterTileEntity smelter;

    // client
    public MetalSmelterMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        super(RegistryBlocks.METAL_SMELTER_MENU, containerId);
        BlockEntity t = inv.player.level.getBlockEntity(buf.readBlockPos());
        smelter = t instanceof MetalSmelterTileEntity ? (MetalSmelterTileEntity) t : null;
    }

    // server
    public MetalSmelterMenu(int containerId, Inventory inv, MetalSmelterTileEntity tile) {
        super(RegistryBlocks.METAL_SMELTER_MENU, containerId);
        smelter = tile;
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
}
