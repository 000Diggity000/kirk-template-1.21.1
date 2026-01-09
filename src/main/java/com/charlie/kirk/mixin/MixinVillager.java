package com.charlie.kirk.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class MixinVillager extends AbstractVillager {
    public MixinVillager(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }
    @Inject(method="mobInteract", at = @At("HEAD"))
    protected void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        //boolean flag = this.getOffers().isEmpty();
        if(itemstack.is(Items.LEAD))
        {
            itemstack.setCount(itemstack.getCount() - 1);
            if(!this.level().isClientSide())
            {
                cir.setReturnValue(InteractionResult.CONSUME);
            }

        }
    }

}
