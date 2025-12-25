package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class SiloBlock extends AbstractSiloBlock<SiloBlockEntity> implements EntityBlock {
    public static final EnumProperty<SiloType> TYPE = Kirk.SILO_TYPE;
    public SiloBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends AbstractSiloBlock<SiloBlockEntity>> codec() {
        return Kirk.SIMPLE_CODEC.get();
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        SiloType chesttype = SiloType.SINGLE;
        boolean flag = context.isSecondaryUseActive();
        Direction direction1 = context.getClickedFace();

        if (!flag) {
            if (this.candidatePartnerFacing(context, Direction.DOWN)) {
                chesttype = SiloType.MIDDLE;
                Kirk.LOGGER.info("down");
            } else if (this.candidatePartnerFacing(context, Direction.UP)) {
                Kirk.LOGGER.info("up");
                chesttype = SiloType.BOTTOM;
            }

        }

        return (BlockState)((BlockState)((BlockState)this.defaultBlockState()).setValue(TYPE, chesttype));

    }
    @javax.annotation.Nullable
    private boolean candidatePartnerFacing(BlockPlaceContext context, Direction direction) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction));
        return blockstate.is(this) && blockstate.getValue(TYPE) == SiloType.SINGLE;
    }

    @Override
    public TripleVerticalBlockCombiner.NeighborsCombineResult<? extends SiloBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean var4) {
        return TripleVerticalBlockCombiner.combineWithNeigbours((BlockEntityType)this.blockEntityType.get(), SiloBlock::getBlockType, state, level, pos);
    }
    public static TripleVerticalBlockCombiner.BlockType getBlockType(BlockState state) {
        SiloType chesttype = (SiloType)state.getValue(TYPE);
        if (chesttype == SiloType.SINGLE) {
            return TripleVerticalBlockCombiner.BlockType.SINGLE;
        } else {
            return chesttype == SiloType.TOP ? TripleVerticalBlockCombiner.BlockType.BOTTOM : TripleVerticalBlockCombiner.BlockType.TOP;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SiloBlockEntity(Kirk.SILO_BLOCK_ENTITY.get(), blockPos, blockState);
    }
}
