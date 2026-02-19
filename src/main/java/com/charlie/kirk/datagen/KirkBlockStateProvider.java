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
        simpleBlock(Kirk.SWAMP_BRICKS_BLOCK.get());
        logBlock(Kirk.GUNK_LOG.get());
        simpleBlock(Kirk.GUNK_LEAVES.get());
        simpleBlock(Kirk.MOSSY_SWAMP_BRICKS_BLOCK.get());
        simpleBlock(Kirk.SEWER_DIRT.get());
    }
}
