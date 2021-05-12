package cc.sukazyo.sericons.loot;

import cc.sukazyo.sericons.register.RegistryBlocks;
import cc.sukazyo.sericons.register.RegistryItems;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import javax.annotation.Nonnull;
import java.util.Set;

public class ModBlockLoot extends BlockLoot {

    private final Set<Block> knownBlocks = new ObjectArraySet<>();

    @Nonnull
    @Override
    public final Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected final void add(@Nonnull Block blockIn, @Nonnull LootTable.Builder table) {
        super.add(blockIn, table);
        knownBlocks.add(blockIn);
    }

    @Override
    protected void addTables() {
        add(RegistryBlocks.FELDSPAR, LootTable.lootTable().withPool(
                applyExplosionCondition(RegistryItems.FELDSPAR, LootPool.lootPool()
                        .setRolls(
                                new RandomValueBounds(1.0f, 7.0f))
                        .add(LootItem.lootTableItem(
                                RegistryItems.FELDSPAR))
                ))
        );
    }
}
