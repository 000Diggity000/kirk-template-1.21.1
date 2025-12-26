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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
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
    public static final EnumProperty<SiloType> TYPE = EnumProperty.create("type", SiloType.class);
    public SiloBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(TYPE, SiloType.SINGLE)
        );
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        // this is where the properties are actually added to the state
        pBuilder.add(TYPE);
    }

    @Override
    protected MapCodec<? extends AbstractSiloBlock<SiloBlockEntity>> codec() {
        return Kirk.SIMPLE_CODEC.get();
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        SiloType chesttype = SiloType.SINGLE;
        boolean flag = context.isSecondaryUseActive();
        Direction direction1 = context.getClickedFace();
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(Direction.DOWN));
        BlockState blockstate1 = context.getLevel().getBlockState(context.getClickedPos().relative(Direction.UP));
        if (!flag) {
            if (blockstate.is(this) && blockstate.getValue(TYPE) == SiloType.SINGLE) {
                if(blockstate1.is(this) && blockstate.getValue(TYPE) == SiloType.SINGLE)
                {
                    if(context.getLevel().getBlockState(context.getClickedPos().relative(Direction.DOWN).relative(Direction.DOWN)).is(this) && context.getLevel().getBlockState(context.getClickedPos().relative(Direction.DOWN).relative(Direction.DOWN)).getValue(TYPE) == SiloType.SINGLE)
                    {
                        Kirk.LOGGER.info("top");
                        chesttype = SiloType.TOP;
                    }else{
                        Kirk.LOGGER.info("middle");
                        chesttype = SiloType.MIDDLE;
                    }

                }else if(!blockstate1.isAir())
                {
                    Kirk.LOGGER.info("middle");
                    chesttype = SiloType.MIDDLE;
                }else{
                    Kirk.LOGGER.info("middle");
                    chesttype = SiloType.MIDDLE;
                }

            }else if (blockstate.is(this) && blockstate.getValue(TYPE) == SiloType.MIDDLE) {
                Kirk.LOGGER.info("top");
                chesttype = SiloType.TOP;
            }
            else if (blockstate.is(this) && blockstate1.getValue(TYPE) == SiloType.MIDDLE) {
                Kirk.LOGGER.info("bottom");
                chesttype = SiloType.BOTTOM;
            }

        }

        return (BlockState)((BlockState)((BlockState)this.defaultBlockState()).setValue(TYPE, chesttype));

    }
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState downBlock = level.getBlockState(currentPos.relative(Direction.DOWN));
        BlockState upBlock = level.getBlockState(currentPos.relative(Direction.UP));
        BlockState upUpBlock = level.getBlockState(currentPos.relative(Direction.UP).relative(Direction.UP));
        SiloType siloTypeDown = null;
        SiloType siloTypeUp = null;
        SiloType siloTypeUpUp = null;
        if(downBlock.is(this))
        {
            siloTypeDown = (SiloType)downBlock.getValue(TYPE);
        }
        if(upBlock.is(this))
        {
            siloTypeUp = (SiloType)upBlock.getValue(TYPE);
        }
        if(upUpBlock.is(this))
        {
            siloTypeUpUp = (SiloType)upUpBlock.getValue(TYPE);
        }
        if (siloTypeUp != null && siloTypeDown != null) {
            if (state.getValue(TYPE) == SiloType.SINGLE && siloTypeUp == SiloType.SINGLE && siloTypeDown == SiloType.SINGLE) {
                Kirk.LOGGER.info("middle");
                return (BlockState)state.setValue(TYPE, SiloType.MIDDLE);
            }else if(state.getValue(TYPE) == SiloType.SINGLE && siloTypeUp == SiloType.MIDDLE && siloTypeUpUp == SiloType.TOP)
            {
                Kirk.LOGGER.info("top");
                return (BlockState)state.setValue(TYPE, SiloType.BOTTOM);
            }else if(siloTypeUpUp != null)
            {
                if(state.getValue(TYPE) == SiloType.SINGLE && siloTypeUp == SiloType.SINGLE && siloTypeUpUp == SiloType.SINGLE)
                {
                    Kirk.LOGGER.info("top");
                    return (BlockState)state.setValue(TYPE, SiloType.BOTTOM);
                }
            }
        }else if(siloTypeUp != null)
        {
            if(state.getValue(TYPE) == SiloType.SINGLE && siloTypeUp == SiloType.MIDDLE)
            {
                Kirk.LOGGER.info("bottom");
                return (BlockState)state.setValue(TYPE, SiloType.BOTTOM);
            }
        }else if(siloTypeDown != null)
        {
            if(state.getValue(TYPE) == SiloType.SINGLE && siloTypeDown == SiloType.MIDDLE)
            {
                Kirk.LOGGER.info("top");
                return (BlockState)state.setValue(TYPE, SiloType.TOP);
            }
        }
        if(facingState.isAir() && facing == Direction.DOWN && state.getValue(TYPE) == SiloType.TOP)
        {
            Kirk.LOGGER.info("single from top");
            return (BlockState)state.setValue(TYPE, SiloType.SINGLE);
        }
        if(facingState.isAir() && facing == Direction.UP && state.getValue(TYPE) == SiloType.BOTTOM)
        {
            Kirk.LOGGER.info("single from bottom");
            return (BlockState)state.setValue(TYPE, SiloType.SINGLE);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
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
