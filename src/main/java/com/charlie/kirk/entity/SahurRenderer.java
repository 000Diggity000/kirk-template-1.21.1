package com.charlie.kirk.entity;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SahurRenderer extends AbstractZombieRenderer<Zombie, ZombieModel<Zombie>> {
    public SahurRenderer(EntityRendererProvider.Context p_174456_) {
        this(p_174456_, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public SahurRenderer(EntityRendererProvider.Context context, ModelLayerLocation zombieLayer, ModelLayerLocation innerArmor, ModelLayerLocation outerArmor) {
        super(context, new ZombieModel(context.bakeLayer(zombieLayer)), new ZombieModel(context.bakeLayer(innerArmor)), new ZombieModel(context.bakeLayer(outerArmor)));
    }
}
