package com.charlie.kirk.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class SiloBlock extends AbstractSiloBlock<SiloBlockEntity>{
    protected SiloBlock(Properties properties, Supplier<BlockEntityType<? extends SiloBlockEntity>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    protected MapCodec<? extends AbstractChestBlock<SiloBlockEntity>> codec() {
        return null;
    }

    @Override
    public TripleVerticalBlockCombiner.NeighborsCombineResult<? extends SiloBlockEntity> combine(BlockState var1, Level var2, BlockPos var3, boolean var4) {
        return TripleVerticalBlockCombiner.combineWithNeigbours((BlockEntityType)this.blockEntityType.get(), SiloBlock::getBlockType, ChestBlock::getConnectedDirection, FACING, state, level, pos);
    }
    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
        ChestType chesttype = (ChestType)state.getValue(TYPE);
        if (chesttype == ChestType.SINGLE) {
            return DoubleBlockCombiner.BlockType.SINGLE;
        } else {
            return chesttype == ChestType.RIGHT ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean override) {
        BiPredicate<LevelAccessor, BlockPos> bipredicate;
        if (override) {
            bipredicate = (p_51578_, p_51579_) -> false;
        } else {
            bipredicate = ChestBlock::isChestBlockedAt;
        }

        return DoubleBlockCombiner.combineWithNeigbour((BlockEntityType)this.blockEntityType.get(), ChestBlock::getBlockType, ChestBlock::getConnectedDirection, FACING, state, level, pos, bipredicate);
    }
}
