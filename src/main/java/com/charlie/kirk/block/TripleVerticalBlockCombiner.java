package com.charlie.kirk.block;

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
    public static <S extends BlockEntity> NeighborsCombineResult<S> combineWithNeigbours(BlockEntityType<S> blockEntityType, Function<BlockState, TripleVerticalBlockCombiner.BlockType> doubleBlockTypeGetter, BlockState state, LevelAccessor level, BlockPos pos) {
        S s = blockEntityType.getBlockEntity(level, pos);
        if (s == null) {
            return Combiner::acceptNone;
        } else {
            TripleVerticalBlockCombiner.BlockType doubleblockcombiner$blocktype = (TripleVerticalBlockCombiner.BlockType)doubleBlockTypeGetter.apply(state);
            boolean flag = doubleblockcombiner$blocktype == TripleVerticalBlockCombiner.BlockType.SINGLE;
            boolean flag1 = doubleblockcombiner$blocktype == BlockType.TOP;
            boolean flag2 = doubleblockcombiner$blocktype == BlockType.MIDDLE;
            boolean flag3 = doubleblockcombiner$blocktype == BlockType.BOTTOM;
            if (flag) {
                return new NeighborsCombineResult.Single<S>(s);
            } else {
                BlockPos blockpos = pos.relative(Direction.UP);
                BlockState blockstate = level.getBlockState(blockpos);
                if (blockstate.is(state.getBlock())) {
                    TripleVerticalBlockCombiner.BlockType doubleblockcombiner$blocktype1 = (TripleVerticalBlockCombiner.BlockType)doubleBlockTypeGetter.apply(blockstate);
                    if (doubleblockcombiner$blocktype1 != TripleVerticalBlockCombiner.BlockType.SINGLE && doubleblockcombiner$blocktype != doubleblockcombiner$blocktype1) {

                        S s1 = blockEntityType.getBlockEntity(level, blockpos);
                        if (s1 != null) {
                            S s2 = flag1 ? s : s1;
                            S s3 = flag2 ? s : s1;
                            S s4 = flag3 ? s : s1;
                            return new NeighborsCombineResult.Triple<S>(s2, s3, s4);
                        }
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

            public Triple(S first, S second, S bottom) {
                this.top = first;
                this.middle = second;
                this.bottom = bottom;
            }

            public <T> T apply(Combiner<? super S, T> combiner) {
                return combiner.acceptTriple(this.top, this.middle, this.bottom);
            }
        }

        public static final class Single<S> implements NeighborsCombineResult<S> {
            private final S single;

            public Single(S single) {
                this.single = single;
            }

            public <T> T apply(Combiner<? super S, T> combiner) {
                return combiner.acceptSingle(this.single);
            }
        }
    }
}
