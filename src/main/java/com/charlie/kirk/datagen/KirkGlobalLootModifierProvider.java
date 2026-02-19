package com.charlie.kirk.datagen;

import com.charlie.kirk.Kirk;
import com.mojang.datafixers.types.templates.Check;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KirkGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public KirkGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Kirk.MODID);
    }
    // Get the PackOutput from GatherDataEvent.


    @Override
    protected void start() {
        // Call #add to add a new GLM. This also adds a corresponding entry in global_loot_modifiers.json.
        add(
                // The name of the modifier. This will be the file name.
                "oak_log_modifier",
                // The loot modifier to add. For the sake of example, we add a weather loot condition.
                new AddTableLootModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.OAK_LOG).build(), LootItemRandomChanceCondition.randomChance(0.2f).build(), ExplosionCondition.survivesExplosion().build()
                }, Kirk.RESIN_TABLE),

                // A list of data load conditions. Note that these are unrelated to the loot conditions
                // specified on the modifier itself. For the sake of example, we add a mod loaded condition.
                // An overload of #add is available that accepts a vararg of conditions instead of a list.
                List.of(new ModLoadedCondition(Kirk.MODID))
        );
        add(
                // The name of the modifier. This will be the file name.
                "spruce_log_modifier",
                // The loot modifier to add. For the sake of example, we add a weather loot condition.
                new AddTableLootModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SPRUCE_LOG).build(), LootItemRandomChanceCondition.randomChance(0.75f).build(), ExplosionCondition.survivesExplosion().build()
                }, Kirk.CORE_WOOD_TABLE),

                // A list of data load conditions. Note that these are unrelated to the loot conditions
                // specified on the modifier itself. For the sake of example, we add a mod loaded condition.
                // An overload of #add is available that accepts a vararg of conditions instead of a list.
                List.of(new ModLoadedCondition(Kirk.MODID))
        );
    }
}
