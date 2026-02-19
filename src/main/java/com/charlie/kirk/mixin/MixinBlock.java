package com.charlie.kirk.mixin;

import com.charlie.kirk.Kirk;
import com.charlie.kirk.block.LogBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotatedPillarBlock.class)
public class MixinBlock extends Block {

    public MixinBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if(state.getBlock() == Blocks.OAK_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.OAK), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.BIRCH_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.BIRCH), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.DARK_OAK_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.DARK_OAK), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.ACACIA_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.ACACIA), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.SPRUCE_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.SPRUCE), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.JUNGLE_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.JUNGLE), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.MANGROVE_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.MANGROVE), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }else if(state.getBlock() == Blocks.CHERRY_LOG)
        {
            BlockState blockstate = pushEntitiesUp(state, Kirk.LOG_BLOCK.get().copyLog(state.getValue(RotatedPillarBlock.AXIS), LogBlock.LogType.CHERRY), level, pos);
            level.setBlockAndUpdate(pos, blockstate);
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

}
