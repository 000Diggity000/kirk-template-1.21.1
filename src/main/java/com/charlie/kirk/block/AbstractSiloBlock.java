package com.charlie.kirk.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class AbstractSiloBlock<E extends BlockEntity> extends BaseEntityBlock {
    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;

    protected AbstractSiloBlock(BlockBehaviour.Properties properties, Supplier<BlockEntityType<? extends E>> blockEntityType) {
        super(properties);
        this.blockEntityType = blockEntityType;
    }

    protected abstract MapCodec<? extends AbstractChestBlock<E>> codec();

    public abstract TripleVerticalBlockCombiner.NeighborsCombineResult<? extends SiloBlockEntity> combine(BlockState var1, Level var2, BlockPos var3, boolean var4);
}
