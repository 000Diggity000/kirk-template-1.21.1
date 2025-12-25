package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class AbstractSiloBlock<E extends BlockEntity> extends BaseEntityBlock {
    protected final Supplier<BlockEntityType<SiloBlockEntity>> blockEntityType = Kirk.SILO_BLOCK_ENTITY;

    protected AbstractSiloBlock(Properties properties) {
        super(properties);
    }

    protected abstract MapCodec<? extends AbstractSiloBlock<E>> codec();

    public abstract TripleVerticalBlockCombiner.NeighborsCombineResult<? extends SiloBlockEntity> combine(BlockState var1, Level var2, BlockPos var3, boolean var4);
}
