package cc.sukazyo.sericons.api;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class StackSlotHandler implements IItemHandlerModifiable {
    int size;
    IItemSlotWrapper wrapper;
    int index;
    boolean[] canInsert;
    boolean[] canExtract;

    public StackSlotHandler(int size, IItemSlotWrapper wrapper, int index, boolean[] canInsert, boolean[] canExtract) {
        this.size = size;
        this.wrapper = wrapper;
        this.index = index;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    public StackSlotHandler(int size, IItemSlotWrapper wrapper) {
        this(size, wrapper, 0, new boolean[size], new boolean[size]);
        for (int i = 0; i < size; i++) {
            this.canExtract[i] = this.canInsert[i] = true;

        }
    }

    public StackSlotHandler(int size, IItemSlotWrapper wrapper, int index, boolean canInsert, boolean canExtract) {
        this(size, wrapper, index, new boolean[size], new boolean[size]);
        for (int i = 0; i < size; i++) {
            this.canInsert[i] = canInsert;
            this.canExtract[i] = canExtract;
        }
    }

    @Override
    public int getSlots() {
        return size;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int i) {
        return this.wrapper.slots().get(this.index + i);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
        if (!canInsert[i] || arg.isEmpty()) {
            return arg;
        }
        arg = arg.copy();
        if (!wrapper.canInsert(this.index + i, arg)) {
            return arg;
        }
        int offset = this.index + i;
        ItemStack curr = this.wrapper.slots().get(offset);
        if (curr.isEmpty()) {
            int accepted = Math.min(arg.getMaxStackSize(), this.wrapper.getMaxStackSize(offset));
            if (accepted < arg.getCount()) {
                if (!bl) {
                    wrapper.slots().set(offset, arg.split(accepted));
                    wrapper.render(offset);
                } else {
                    arg.shrink(accepted);
                }
                return arg;
            } else {
                if (!bl) {
                    wrapper.slots().set(offset, arg);
                    wrapper.render(offset);
                }
                return ItemStack.EMPTY;
            }
        } else {
            if (!ItemHandlerHelper.canItemStacksStack(arg, curr)) {
                return arg;
            }
            int accepted = Math.min(arg.getMaxStackSize(), wrapper.getMaxStackSize(offset)) - curr.getCount();
            if (accepted < arg.getCount()) {
                if (!bl) {
                    ItemStack resutl = arg.split(accepted);
                    resutl.grow(curr.getCount());
                    wrapper.slots().set(offset, resutl);
                    wrapper.render(offset);
                } else {
                    arg.shrink(accepted);
                }
                return arg;
            } else {
                if (!bl) {
                    ItemStack resutl = arg.copy();
                    resutl.grow(curr.getCount());
                    wrapper.slots().set(offset, resutl);
                    wrapper.render(offset);
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int i, int j, boolean bl) {
        if (!canExtract[i] || j == 0) {
            return ItemStack.EMPTY;
        }
        int offset = this.index + i;
        ItemStack curr = wrapper.slots().get(offset);
        if (curr.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int extracted = Math.min(curr.getCount(), j);
        ItemStack cp = curr.copy();
        cp.setCount(extracted);
        if (!bl) {
            if (extracted < curr.getCount()) {
                curr.shrink(extracted);
            } else {
                curr = ItemStack.EMPTY;
            }
            wrapper.slots().set(offset, curr);
            wrapper.render(offset);
        }
        return cp;
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return true;
    }

    @Override
    public void setStackInSlot(int i, @NotNull ItemStack arg) {
        wrapper.slots().set(this.index + i, arg);
        wrapper.render(this.index + i);
    }
}
