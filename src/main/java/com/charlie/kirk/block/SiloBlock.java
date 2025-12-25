package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SiloBlock extends AbstractSiloBlock<SiloBlockEntity>{
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
    public SiloBlock(Properties properties, Supplier<BlockEntityType<? extends SiloBlockEntity>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    protected MapCodec<? extends AbstractChestBlock<SiloBlockEntity>> codec() {
        //return Kirk.SIMPLE_CODEC.get();
        return null;
    }

    @Override
    public TripleVerticalBlockCombiner.NeighborsCombineResult<? extends SiloBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean var4) {
        return TripleVerticalBlockCombiner.combineWithNeigbours((BlockEntityType)this.blockEntityType.get(), SiloBlock::getBlockType, state, level, pos);
    }
    public static TripleVerticalBlockCombiner.BlockType getBlockType(BlockState state) {
        ChestType chesttype = (ChestType)state.getValue(TYPE);
        if (chesttype == ChestType.SINGLE) {
            return TripleVerticalBlockCombiner.BlockType.SINGLE;
        } else {
            return chesttype == ChestType.RIGHT ? TripleVerticalBlockCombiner.BlockType.BOTTOM : TripleVerticalBlockCombiner.BlockType.TOP;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SiloBlockEntity(Kirk.SILO_BLOCK_ENTITY.get(), blockPos, blockState);
    }
}
