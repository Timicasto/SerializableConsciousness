package cc.sukazyo.sericons.fluid;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.register.RegistryFluids;
import cc.sukazyo.sericons.register.RegistryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class SteamFluid extends FlowingFluid {

    public static class Source extends SteamFluid {

        @Override
        public boolean isSource (FluidState state) {
            return true;
        }

    }

    public static class Flowing extends SteamFluid {

        @Override
        public boolean isSource (FluidState state) {
            return false;
        }

    }

    @Override
    public Fluid getFlowing() {
        return RegistryFluids.STEAM_FLUID_FLOWING;
    }

    @Override
    public Fluid getSource() {
        return RegistryFluids.STEAM_FLUID;
    }

    @Override
    protected boolean canConvertToSource() {
        return true;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor arg, BlockPos arg2, BlockState arg3) {

    }

    @Override
    protected int getSlopeFindDistance(LevelReader arg) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader arg) {
        return 3;
    }

    @Override
    public Item getBucket() {
        return RegistryItems.BUCKET_STEAM;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState arg, BlockGetter arg2, BlockPos arg3, Fluid arg4, Direction arg5) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader arg) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState arg) {
        return null;
    }

    @Override
    public boolean isSource(FluidState arg) {
        return false;
    }

    @Override
    public int getAmount(FluidState arg) {
        return 0;
    }

    @Override
    protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(
                new ResourceLocation(SeriConsMod.MODID, "textures/fluids/steam"),
                new ResourceLocation(SeriConsMod.MODID, "textures/fluids/steam")
        ).density(400).build(this);
    }
}
