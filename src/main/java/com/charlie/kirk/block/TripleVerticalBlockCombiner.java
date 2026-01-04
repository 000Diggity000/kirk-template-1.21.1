package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class TripleVerticalBlockCombiner {
    public static <S extends BlockEntity> NeighborsCombineResult<S> combineWithNeigbours(BlockEntityType<S> blockEntityType, Function<BlockState, BlockType> doubleBlockTypeGetter, BlockState state, LevelAccessor level, BlockPos pos) {
        S s = blockEntityType.getBlockEntity(level, pos);
        Kirk.LOGGER.info("idk");
        if (s == null) {
            return Combiner::acceptNone;
        } else {
            BlockType doubleblockcombiner$blocktype = (BlockType)doubleBlockTypeGetter.apply(state);
            boolean flag = doubleblockcombiner$blocktype == BlockType.SINGLE;
            boolean flag1 = doubleblockcombiner$blocktype == BlockType.TOP;
            boolean flag2 = doubleblockcombiner$blocktype == BlockType.MIDDLE;
            boolean flag3 = doubleblockcombiner$blocktype == BlockType.BOTTOM;
            S s2 = null;
            S s3 = null;
            S s4 = null;
            if (flag) {
                return new NeighborsCombineResult.Single<S>(s);
            } else {
                BlockPos blockpos = pos.relative(Direction.UP);
                BlockPos blockpos1 = pos.relative(Direction.DOWN);
                BlockState blockstate = level.getBlockState(blockpos);
                BlockState blockstate1 = level.getBlockState(blockpos1);
                if (blockstate.is(state.getBlock())) {
                    BlockType tripleblockcombiner$blocktype1 = (BlockType)doubleBlockTypeGetter.apply(blockstate);
                    BlockType tripleblockcombiner$blocktype2 = (BlockType)doubleBlockTypeGetter.apply(blockstate1);
                    if (tripleblockcombiner$blocktype1 != BlockType.SINGLE && doubleblockcombiner$blocktype != tripleblockcombiner$blocktype1) {
                        S s1 = blockEntityType.getBlockEntity(level, blockpos);
                        if (s1 != null) {
                            s2 = flag2 ? s : s1;
                            s3 = flag3 ? s : s1;

                        }
                    }else{
                        Kirk.LOGGER.info("idk");
                    }
                    if (tripleblockcombiner$blocktype1 != BlockType.SINGLE && doubleblockcombiner$blocktype != tripleblockcombiner$blocktype1) {
                        S s5 = blockEntityType.getBlockEntity(level, blockpos1);
                        if (s5 != null) {
                            s3 = flag1 ? s : s5;
                            s4 = flag2 ? s : s5;

                        }
                    }
                    if(s2 != null && s3 != null && s4 != null)
                    {
                        return new NeighborsCombineResult.Triple<S>(s2, s3, s4);
                    }
                }

                return new NeighborsCombineResult.Single<S>(s);
            }
        }
    }
//done
    public static enum BlockType {
        SINGLE,
        TOP,
        MIDDLE,
        BOTTOM;
    }

    public interface Combiner<S, T> {
        T acceptTriple(S var1, S var2, S var3);

        T acceptSingle(S var1);

        T acceptNone();
    }

    public interface NeighborsCombineResult<S> {
        <T> T apply(Combiner<? super S, T> var1);

        public static final class Triple<S> implements NeighborsCombineResult<S> {
            private final S top;
            private final S middle;
            private final S bottom;

            public Triple(S top, S middle, S bottom) {
                this.top = top;
                this.middle = middle;
                this.bottom = bottom;
            }

            public <T> T apply(Combiner<? super S, T> combiner) {
                Kirk.LOGGER.info("idk");
                return combiner.acceptTriple(this.top, this.middle, this.bottom);
            }
        }

        public static final class Single<S> implements NeighborsCombineResult<S> {
            private final S single;

            public Single(S single) {
                this.single = single;
            }

            public <T> T apply(Combiner<? super S, T> combiner) {
                Kirk.LOGGER.info("idk");
                return combiner.acceptSingle(this.single);
            }
        }
    }
}
