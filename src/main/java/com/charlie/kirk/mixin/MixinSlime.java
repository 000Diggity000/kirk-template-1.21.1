package com.charlie.kirk.mixin;

import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slime.class)
public class MixinSlime {
    @Shadow
    public static final int MAX_NATURAL_SIZE = 10;
}
