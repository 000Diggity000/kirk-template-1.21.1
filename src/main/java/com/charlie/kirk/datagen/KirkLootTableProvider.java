package com.charlie.kirk.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KirkLootTableProvider extends LootTableProvider {
    public KirkLootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(
                        // A reference to the sub provider's constructor.
                        // This is a Function<HolderLookup.Provider, ? extends LootTableSubProvider>.
                        KirkBlockLootTableProvider::new,
                        // An associated loot context set. If you're unsure what to use, use empty.
                        LootContextParamSets.BLOCK
                )
        ), registries);
    }
}
