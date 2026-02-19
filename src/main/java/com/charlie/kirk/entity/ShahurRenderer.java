package com.charlie.kirk.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShahurRenderer extends MobRenderer<SahurMob, SahurEntityModel<SahurMob>> {


    public ShahurRenderer(EntityRendererProvider.Context context) {
        super(context,  new SahurEntityModel<>(context.bakeLayer(SahurEntityModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(SahurMob sahurMob) {
        return SahurEntityModel.LAYER_LOCATION .getModel();
    }
}
