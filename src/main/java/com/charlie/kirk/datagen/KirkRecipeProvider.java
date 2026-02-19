package com.charlie.kirk.datagen;

import com.charlie.kirk.Kirk;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;


public class KirkRecipeProvider extends RecipeProvider {

    // Construct the provider to run
    public KirkRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        //ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TORCH, 2).define('R', Kirk.RESIN.get()).define('S', Items.STICK).pattern("R").pattern("S").unlockedBy("has_resin", has(Kirk.RESIN.get())).save(output);
        
    }

}
