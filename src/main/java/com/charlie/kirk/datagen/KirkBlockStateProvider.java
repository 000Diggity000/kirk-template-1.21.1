package com.charlie.kirk.datagen;

import com.charlie.kirk.Kirk;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class KirkBlockStateProvider extends BlockStateProvider {
    public KirkBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Kirk.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Kirk.GUNK_BLOCK.get());
    }
}
