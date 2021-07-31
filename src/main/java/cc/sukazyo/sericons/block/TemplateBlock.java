package cc.sukazyo.sericons.block;

import cc.sukazyo.sericons.api.multiblock.IEnumPropertyBlock;
import cc.sukazyo.sericons.api.multiblock.IStateBlock;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TemplateBlock<E extends Enum<E> & IEnumPropertyBlock> extends Block implements IStateBlock {
    protected static Property[] properties;

    public final String name;
    public final EnumProperty<E> property;
    public final Property[] additional;
    public final E[] values;
    boolean[] hasFlavor;
    protected Set<RenderType>[] stateTypes;
    protected Map<BlockState, Integer> opacities;
    protected Map<BlockState, Float> strength;
    protected boolean[] canBeDisassembled;
    protected boolean[] notNormalBlock;
    private boolean opaqueCube = false;

    public TemplateBlock(String name, Material material, EnumProperty<E> property, Object... additional) {
        super(Properties.of(genMaterial(material, property, additional)));
        this.name = name;
        this.property = property;
        this.values = property.getValueClass().getEnumConstants();
        this.hasFlavor = new boolean[this.values.length];
        this.stateTypes = new Set[this.values.length];
        this.canBeDisassembled = new boolean[this.values.length];

        List<Property> properties = new ArrayList<>();
        for (Object o : additional) {
            if (o instanceof Property) {
                properties.add((Property)o);
            }
            if (o instanceof Property[]) {
                for (Property property1 : (Property[]) o) {
                    properties.add(property1);
                }
            }
        }
        this.additional = properties.toArray(new Property[properties.size()]);
        this.registerDefaultState(defaultState());
        //this.setRegistryName(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Property property() {
        return null;
    }

    @Override
    public Enum[] enums() {
        return new Enum[0];
    }

    public E[] getValues() {
        return values;
    }

    @Override
    public BlockState state(int i) {
        BlockState state = this.stateDefinition.any().setValue(this.property, values[i]);
        return state;
    }

    public EnumProperty<E> getProperty() {
        return property;
    }

    @Override
    public boolean customMapper() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String customMapping(int i) {
        return null;
    }

    @Override
    public boolean appendProperty() {
        return true;
    }

    protected static Material genMaterial(Material material, EnumProperty<?> property, Object... additional) {
        List<Property> properties = new ArrayList<>();
        properties.add(property);
        for (Object o : additional) {
            if (o instanceof Property) {
                properties.add((Property) o);
            }
            if (o instanceof Property[]) {
                for (Property prop : (Property[]) o) {
                    properties.add(prop);
                }
            }
        }
        TemplateBlock.properties = properties.toArray(new Property[properties.size()]);
        return material;
    }

    protected BlockState defaultState() {
        BlockState state = this.stateDefinition.any().setValue(property, values[0]);
        for (int i = 0; i < additional.length; i++) {
            if (this.additional[i] != null && !this.additional[i].getPossibleValues().isEmpty()) {
                state = genState(state, additional[i], additional[i].getPossibleValues().iterator().next());
            }
        }
        return state;
    }

    @Override
    protected abstract void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

    protected static Object[] combine(Object[] curr, Object... tar) {
        Object[] arr = new Object[curr.length + tar.length];
        for (int i = 0; i < curr.length; i++) {
            arr[i] = curr[i];
        }
        for (int i = 0; i < tar.length; i++) {
            arr[curr.length + i] = tar[i];
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    protected <P extends Comparable<P>> BlockState genState(BlockState in, Property<P> property, Object tar) {
        return in.setValue(property, (P)tar);
    }
}
