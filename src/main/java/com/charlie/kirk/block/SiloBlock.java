package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.charlie.kirk.menu.SiloMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class SiloBlock extends AbstractSiloBlock<SiloBlockEntity> implements EntityBlock {
    public static final MapCodec<SiloBlock> CODEC = simpleCodec(SiloBlock::new);
    public static final EnumProperty<SiloType> TYPE = EnumProperty.create("type", SiloType.class);
    private static final VoxelShape SINGLE_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape BOTTOM_SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape TOP_SHAPE = Block.box(1, 0, 1, 15, 8, 15);
    public SiloBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(TYPE, SiloType.SINGLE)
        );
    }
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(state.getValue(TYPE) == SiloType.SINGLE)
            {
                return InteractionResult.FAIL;
            }
            MenuProvider menuprovider = this.getMenuProvider(state, level, pos);
            if (menuprovider != null) {
                player.openMenu(menuprovider);
            }

            return InteractionResult.CONSUME;
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        // this is where the properties are actually added to the state
        pBuilder.add(TYPE);
    }

    @Override
    protected MapCodec<? extends AbstractSiloBlock<SiloBlockEntity>> codec() {
        return CODEC;
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(TYPE, selectCorrectType(context.getLevel(), context.getClickedPos(), context.getLevel().getBlockState(context.getClickedPos())));

        //return (BlockState)((BlockState)((BlockState)this.defaultBlockState()).setValue(TYPE, chesttype));

    }
    public SiloType selectCorrectType(LevelAccessor level, BlockPos pos, BlockState state)
    {
        BlockState downBlock = level.getBlockState(pos.relative(Direction.DOWN));
        BlockState downDownBlock = level.getBlockState(pos.relative(Direction.DOWN).relative(Direction.DOWN));
        BlockState upBlock = level.getBlockState(pos.relative(Direction.UP));
        BlockState upUpBlock = level.getBlockState(pos.relative(Direction.UP).relative(Direction.UP));
        if(downBlock.is(this) && (downBlock.getValue(TYPE) == SiloType.SINGLE || downBlock.getValue(TYPE) == SiloType.BOTTOM) && upBlock.is(this) && (upBlock.getValue(TYPE) == SiloType.SINGLE || upBlock.getValue(TYPE) == SiloType.TOP))
        {
            Kirk.LOGGER.info("middle");
            Kirk.LOGGER.info(String.valueOf(pos.getY()));
            return SiloType.MIDDLE;
        }else if(downBlock.is(this) && (downBlock.getValue(TYPE) == SiloType.SINGLE || downBlock.getValue(TYPE) == SiloType.MIDDLE) && downDownBlock.is(this) && (downDownBlock.getValue(TYPE) == SiloType.SINGLE || downDownBlock.getValue(TYPE) == SiloType.BOTTOM))
        {
            Kirk.LOGGER.info("top");
            Kirk.LOGGER.info(String.valueOf(pos.getY()));
            return SiloType.TOP;
        }else if(upBlock.is(this) && (upBlock.getValue(TYPE) == SiloType.SINGLE || upBlock.getValue(TYPE) == SiloType.MIDDLE) && upUpBlock.is(this) && (upUpBlock.getValue(TYPE) == SiloType.SINGLE || upUpBlock.getValue(TYPE) == SiloType.TOP))
        {
            Kirk.LOGGER.info("bottom");
            Kirk.LOGGER.info(String.valueOf(pos.getY()));
            return SiloType.BOTTOM;
        }
        return SiloType.SINGLE;
    }
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return (BlockState)this.defaultBlockState().setValue(TYPE, selectCorrectType(level, currentPos, state));

        //return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
    @javax.annotation.Nullable
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if(state.getValue(TYPE) == SiloType.SINGLE)
        {
            return null;
        }else if(state.getValue(TYPE) == SiloType.MIDDLE)
        {
            return new MenuProvider() {
                @javax.annotation.Nullable
                public AbstractContainerMenu createMenu(int p_51622_, Inventory p_51623_, Player p_51624_) {
                        return SiloMenu.sixRows(p_51622_, p_51623_);
                }

                public Component getDisplayName() {
                    return Component.translatable("kirk.container.silo");
                }
            };
        }else {
            return (MenuProvider) this;
        }
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

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case SiloType.SINGLE -> SINGLE_SHAPE;
            case SiloType.BOTTOM, SiloType.MIDDLE -> BOTTOM_SHAPE;
            case SiloType.TOP -> TOP_SHAPE;
            default -> SINGLE_SHAPE;
        };
    }
}
