package cc.sukazyo.sericons.api.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

public interface ITileWithFacing {
    Direction getFacing();
    void setFacing(Direction facing);

    /**
     * @return 0 -> Clicked, 1 -> Piston Acted, 2 -> Horizontal, 3 -> Vertical, 4 -> x/z Axis, 5 -> Horizontal Based on Quadrant, 6 -> Horizontal Preferring Clicked
     */
    int getFacingLimit();

    default Direction getFacing(LivingEntity placer, BlockPos pos, Direction side, float x, float y, float z) {
        Direction facing = Direction.DOWN;
        int limit = getFacingLimit();
        if (limit == 0) {
            facing = side;
        } else if (limit == 1) {
            facing = placer.getDirection();
        } else if (limit == 2) {
            facing = Direction.fromYRot(placer.yHeadRot);
        } else if (limit == 3) {
            facing = (side != Direction.DOWN && (facing == Direction.UP || y <= .5)) ? Direction.UP : Direction.DOWN;
        } else if (limit == 4) {
            facing = Direction.fromYRot(placer.yHeadRot);
            if (facing == Direction.SOUTH || facing == Direction.WEST) {
                facing = facing.getOpposite();
            }
        } else if (limit == 5) {
            if (facing.getAxis() != Direction.Axis.Y) {
                facing = side.getOpposite();
            } else {
                float xTMid = x - 0.5F;
                float zTMid = z - 0.5F;
                float max = Math.max(Math.abs(xTMid), Math.abs(zTMid));
                if (max == Math.abs(xTMid)) {
                    facing = xTMid < 0 ? Direction.WEST : Direction.EAST;
                } else {
                    facing = zTMid < 0 ? Direction.NORTH : Direction.SOUTH;
                }
            }
        } else if (limit == 6) {
            facing = side.getAxis() != Direction.Axis.Y ? side.getOpposite() : placer.getDirection();
        }
        return mirrorFacing(placer) ? facing.getOpposite() : facing;
    }

    boolean mirrorFacing(LivingEntity placer);
    boolean canBeRotated(Direction side, float x, float y, float z, LivingEntity entity);
    boolean canRotate(Direction axis);
    default void rotationFinish(Direction old, Direction newSide) {

    }


}
