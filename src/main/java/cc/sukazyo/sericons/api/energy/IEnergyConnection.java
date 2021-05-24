package cc.sukazyo.sericons.api.energy;

import net.minecraft.core.Direction;

/**
 * Implement This On TileEntities That Can Connect To Energy Transportation Blocks<br>
 * This Is Intended For Blocks That Generate But Do Not Receive It.<br>
 *
 * Note That {@link IEnergyConnector} Is An Extension For This
 */
public interface IEnergyConnection {

    /**
     * @return True If This TileEntity Can Do Energy IO On A Given Facing
     */
    boolean canDoIO(Direction facing);
}
