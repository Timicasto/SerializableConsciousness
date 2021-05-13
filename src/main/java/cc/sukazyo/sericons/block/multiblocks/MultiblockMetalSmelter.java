package cc.sukazyo.sericons.block.multiblocks;

import cc.sukazyo.sericons.api.multiblock.MultiBlockRegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MultiblockMetalSmelter implements MultiBlockRegistryHandler.IMultiblock {
    public static MultiblockMetalSmelter INSTANCE = new MultiblockMetalSmelter();
    static BlockState[][][] structure = new BlockState[5][6][5];

    static {
        BlockState brick = Blocks.BRICKS.defaultBlockState();
        BlockState brickSlab = Blocks.BRICK_SLAB.defaultBlockState();
        BlockState glass = Blocks.GLASS.defaultBlockState();
        BlockState cobbleStoneSlab = Blocks.COBBLESTONE_SLAB.defaultBlockState();
        for (int x = 0 ; x < 5 ; x++) {
            for (int y = 0 ; y < 6 ; y++) {
                for (int z = 0 ; z < 5 ; z++) {
                    if (y == 0) {
                        if ((x == 0 || x == 4) && z != 2) {
                            structure[x][y][z] = brick;
                        }else if ((z == 0 || z == 4) && x != 2) {
                            structure[x][y][z] = brick;
                        }else if ((x == 1 && z == 2) || (x == 2 && z == 1) || (x == 2 && z == 3) || (x == 3 && z == 2)) {
                            structure[x][y][z] = brickSlab;
                        }
                    } else if (y == 1) {
                        if (x == 0 || x == 4) {
                            structure[x][y][z] = brick;
                        } else if (z == 4) {
                            structure[x][y][z] = brick;
                        }else if (z == 0 && x != 2) {
                            structure[x][y][z] = brick;
                        } else if (x == 2 && z == 0) {
                            structure[x][y][z] = glass;
                        }
                    }else if (y == 2) {
                        if ((x == 0 && z != 2) || x == 4 || z == 4 || z == 0) {
                            structure[x][y][z] = brick;
                        }
                    }else if (y == 3) {
                        if (x == 0 || x == 4 || z == 0 || z == 4) {
                            structure[x][y][z] = brick;
                        } else if (x == 1 || x == 3 || z == 1 || z == 3) {
                            structure[x][y][z] = cobbleStoneSlab;
                        }
                    }else if (y == 4) {
                        if (x == 0 || x == 4 || z == 0 || z == 4) {
                            structure[x][y][z] = brick;
                        }
                    }else if (y == 5) {
                        if (x == 0 || x == 4 || z == 0 || z == 4) {
                            structure[x][y][z] = brick;
                        } else if (x == 2) {
                            structure[x][y][z] = brickSlab;
                        } else if (z == 2) {
                            structure[x][y][z] = brickSlab;
                        }
                    }
                    if (structure[x][y][z] == null) {
                        structure[x][y][z] = Blocks.AIR.defaultBlockState();
                    }

                }
            }
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean overwriteRender(ItemStack stack, int iterator) {
        return false;
    }

    @Override
    public boolean canRenderFormedStructure() {
        return true;
    }

    static ItemStack renderStack = ItemStack.EMPTY;

    @Override
    public void renderFormedStructure() {
        //TODO Structure Rendering
    }

    @Override
    public float getManualScale() {
        return 12;
    }

    @Override
    public String getUniqueName() {
        return "sericons:MetalSmelter";
    }

    @Override
    public boolean isTrigger(BlockState state) {
        return state.getBlock() == Blocks.GLASS;
    }

    @Override
    public boolean createStructure(Level level, BlockPos pos, Direction direction, Player player) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            direction = Direction.fromYRot(player.yRot);
        }
        BlockPos start = pos;
        direction = direction.getOpposite();

        boolean mirrored = false;
        boolean isValid = check(level, start, direction, mirrored);

        if (!isValid) {
            mirrored = true;
            isValid = check(level, start, direction, mirrored);
        }

        return isValid;
    }

    boolean check(Level world, BlockPos pos, Direction facing, boolean mirrored) {
        for (int x = 0 ; x < 5 ; x++) {
            for (int z = -2; z < 2 ; z++) {
                for (int y = 0 ; y < 6 ; y++) {
                    if (structure[x][y][z+2].getBlock() == Blocks.AIR) {
                        int zz = mirrored ? -z : z;
                        BlockPos startx = pos.relative(facing, x).relative(Direction.fromYRot(facing.toYRot()), zz).offset(0, y , 0);
                        if (world.getBlockState(startx).getBlock() == Blocks.AIR) {
                            return false;
                        }
                        if (structure[x][y][z+2].getBlock() == Blocks.GLASS) {
                            if (world.getBlockState(startx).getBlock() != Blocks.GLASS) {
                                return false;
                            }
                        }

                    }
                }
            }
        }
    }

    @Override
    public BlockState[][][] getStructure() {
        return structure;
    }
}
