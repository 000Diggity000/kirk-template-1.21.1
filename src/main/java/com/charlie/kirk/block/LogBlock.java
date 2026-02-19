package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class LogBlock extends Block {
    public static final MapCodec<LogBlock> CODEC = simpleCodec(LogBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS;
    public static final EnumProperty<LogType> LOG_TYPE = EnumProperty.create("log_type", LogType.class);

    public MapCodec<? extends LogBlock> codec() {
        return CODEC;
    }

    public LogBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(LOG_TYPE, LogType.OAK));
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        return rotatePillar(state, rot);
    }

    public static BlockState rotatePillar(BlockState state, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)state.getValue(AXIS)) {
                    case X -> {
                        return (BlockState)state.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return (BlockState)state.setValue(AXIS, Direction.Axis.X);
                    }
                    default -> {
                        return state;
                    }
                }
            default:
                return state;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AXIS, LOG_TYPE});
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }
    public BlockState copyLog(Direction.Axis axis, LogType logType) {
        return (BlockState)this.defaultBlockState().setValue(AXIS, axis).setValue(LOG_TYPE, logType);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem)
        {
            if(state.is(Kirk.LOG_BLOCK))
            {
                return switch (state.getValue(LogBlock.LOG_TYPE)) {
                    case LogType.OAK -> Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.BIRCH -> Blocks.STRIPPED_BIRCH_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.DARK_OAK -> Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.ACACIA -> Blocks.STRIPPED_ACACIA_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.SPRUCE -> Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.JUNGLE -> Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.MANGROVE -> Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                    case LogType.CHERRY -> Blocks.STRIPPED_CHERRY_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(LogBlock.AXIS));
                };
            }
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    static {
        AXIS = BlockStateProperties.AXIS;
    }
    public static enum LogType implements StringRepresentable {
        OAK("oak"),
        BIRCH("birch"),
        DARK_OAK("dark_oak"),
        ACACIA("acacia"),
        SPRUCE("spruce"),
        JUNGLE("jungle"),
        MANGROVE("mangrove"),
        CHERRY("cherry");

        private final String name;

        private LogType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
