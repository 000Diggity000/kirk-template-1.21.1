package com.charlie.kirk.entity;

import com.charlie.kirk.Kirk;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SahurEntityModel<T extends SahurMob> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kirk.MODID, "sahur"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightArm; // The "Tung" arm
    private final ModelPart leftArm;

    public SahurEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.rightArm = this.body.getChild("arm_right");
        this.leftArm = this.body.getChild("arm_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 24.0F, 0.0F));

        // The Kentongan (Bamboo Drum)
        body.addOrReplaceChild("kentongan", CubeListBuilder.create().texOffs(24, 0)
                .addBox(-2.0F, -10.0F, -4.0F, 4.0F, 10.0F, 4.0F), PartPose.offset(0.0F, - 8.0F, 0.0F));

        // Right Arm with Beater Stick
        body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(40, 0)
                .addBox(-4.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F)
                .texOffs(0, 16).addBox(-3.0F, 10.0F, -10.0F, 2.0F, 2.0F, 8.0F), PartPose.offset(-5.0F, -12.0F, 0.0F));
        body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(40, 0)
                .addBox(4.0F, 0.0F, -2.0F, -4.0F, 12.0F, 4.0F), PartPose.offset(5.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T t, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animateWalk(limbSwing, limbSwingAmount);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        root.render(poseStack, vertexConsumer, i, i1, i2);
    }
    private void animateWalk(float limbSwing, float limbSwingAmount) {
        float f = Math.min(0.5F, 3.0F * limbSwingAmount);
        float f1 = limbSwing * 0.8662F;
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Math.min(0.35F, f);
        //ModelPart var10000 = this.head;
        //var10000.zRot += 0.3F * f3 * f;
        //this.head.xRot += 1.2F * Mth.cos(f1 + ((float)Math.PI / 2F)) * f4;
        this.body.zRot = 0.1F * f3 * f;
        this.body.xRot = 1.0F * f2 * f4;
        //this.leftLeg.xRot = 1.0F * f2 * f;
        //this.rightLeg.xRot = 1.0F * Mth.cos(f1 + (float)Math.PI) * f;
        this.leftArm.xRot = -(0.8F * -f3 * f);
        this.leftArm.zRot = 0.0F;
        this.rightArm.xRot = -(0.8F * f3 * f);
        this.rightArm.zRot = 0.0F;
        //this.resetArmPoses();
    }
}
