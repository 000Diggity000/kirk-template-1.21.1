package com.charlie.kirk.mixin;

import com.charlie.kirk.Kirk;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Slime.class)
public abstract class MixinSlime extends Mob implements Enemy {
    @Shadow(remap = false)
    public static final int MAX_NATURAL_SIZE = 10;

    protected MixinSlime(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }



    @Overwrite
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setSize(10, true);
        Kirk.LOGGER.info("HELLO from server starting");
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    @Shadow
    public void setSize(int size, boolean resetHealth) {
    }

}
