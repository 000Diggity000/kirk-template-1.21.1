package com.charlie.kirk.datagen;

import com.charlie.kirk.Kirk;
import com.charlie.kirk.block.LogBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;

import java.util.ArrayList;
import java.util.Set;

public class KirkBlockLootTableProvider extends BlockLootSubProvider {
    public KirkBlockLootTableProvider(HolderLookup.Provider lookupProvider) {
        // The first parameter is a set of blocks we are creating loot tables for. Instead of hardcoding,
        // we use our block registry and just pass an empty set here.
        // The second parameter is the feature flag set, this will be the default flags
        // unless you are adding custom flags (which is beyond the scope of this article).
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    // The contents of this Iterable are used for validation.
    // We return an Iterable over our block registry's values here.
    @Override
    protected Iterable<Block> getKnownBlocks() {
        // The contents of our DeferredRegister.
        return Kirk.BLOCKS.getEntries()
                .stream()
                // Cast to Block here, otherwise it will be a ? extends Block and Java will complain.
                .map(e -> (Block) e.value())
                .toList();
    }

    // Actually add our loot tables.
    @Override
    protected void generate() {
        // Equivalent to calling add(MyBlocks.EXAMPLE_BLOCK.get(), createSingleItemTable(MyBlocks.EXAMPLE_BLOCK.get()));
        dropSelf(Kirk.SILO_BLOCK.get());
        dropSelf(Kirk.GUNK_BLOCK.get());
        dropSelf(Kirk.GUNK_LOG.get());
        dropSelf(Kirk.GUNK_LEAVES.get());
        dropSelf(Kirk.SWAMP_BRICKS_BLOCK.get());
        dropSelf(Kirk.MOSSY_SWAMP_BRICKS_BLOCK.get());
        dropSelf(Kirk.SEWER_DIRT.get());
        dropSelf(Kirk.SEWER_FARMLAND.get());
        add(Kirk.LOG_BLOCK.get(), createLogBlockTable(Kirk.LOG_BLOCK.get(), LogBlock.LOG_TYPE, LogBlock.LogType.OAK, LogBlock.LogType.BIRCH, LogBlock.LogType.DARK_OAK, LogBlock.LogType.ACACIA, LogBlock.LogType.JUNGLE, LogBlock.LogType.MANGROVE, LogBlock.LogType.CHERRY, LogBlock.LogType.SPRUCE));
    }
    protected <T extends Comparable<T> & StringRepresentable> LootTable.Builder createLogBlockTable(Block block, Property<T> property, T value, T value1, T value2, T value3, T value4, T value5, T value6, T value7) {
        return LootTable.lootTable().withPool((LootPool.Builder) this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Blocks.OAK_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value))))
                        .add(LootItem.lootTableItem(Blocks.BIRCH_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value1))))
                        .add(LootItem.lootTableItem(Blocks.DARK_OAK_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value2))))
                        .add(LootItem.lootTableItem(Blocks.ACACIA_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value3))))
                        .add(LootItem.lootTableItem(Blocks.JUNGLE_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value4))))
                        .add(LootItem.lootTableItem(Blocks.MANGROVE_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value5))))
                        .add(LootItem.lootTableItem(Blocks.CHERRY_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value6))))
                        .add(LootItem.lootTableItem(Blocks.SPRUCE_LOG).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(property, value7))))
                ));
    }
}
