package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.*;

import java.lang.Math;
import java.util.Objects;
import java.util.Optional;

public class SiloBlockRenderer<T extends SiloBlockEntity> implements BlockEntityRenderer<T> {
    float height;
    // Add the constructor parameter for the lambda below. You may also use it to get some context
    // to be stored in local fields, such as the entity renderer dispatcher, if needed.
    private float rot = 0;
    public SiloBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    // This method is called every frame in order to render the block entity. Parameters are:
    // - blockEntity:   The block entity instance being rendered. Uses the generic type passed to the super interface.
    // - partialTick:   The amount of time, in fractions of a tick (0.0 to 1.0), that has passed since the last tick.
    // - poseStack:     The pose stack to render to.
    // - bufferSource:  The buffer source to get vertex buffers from.
    // - packedLight:   The light value of the block entity.
    // - packedOverlay: The current overlay value of the block entity, usually OverlayTexture.NO_OVERLAY.
    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Matrix4f matrix4f = poseStack.last().pose();
        //Player player = blockEntity.getLevel().getNearestPlayer(blockEntity.getLevel().getEntity(1), (double)32);
        rot = this.rotateTowards(rot, this.getYRotD(100,-100), 1f);
        poseStack.translate(8 / 16.0F, 0 / 16.0F, 8 / 16.0F);
        poseStack.mulPose(new Quaternionf().rotationXYZ(0, rot, 0));
        this.renderCube(blockEntity, matrix4f, bufferSource.getBuffer(this.renderType()));
    }

    private void renderCube(T blockEntity, Matrix4f pose, VertexConsumer consumer) {
        float f = 0;
        float f1 = 0;
        this.renderFace(blockEntity, pose, consumer, -0.5F, 0.5F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.SOUTH);
        this.renderFace(blockEntity, pose, consumer, -0.5F, 0.5F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        //this.renderFace(blockEntity, pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST, player);
        //this.renderFace(blockEntity, pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST, player);
        //this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN, player);
        //this.renderFace(blockEntity, pose, consumer, -0.5F, 0.5F, 1, 1, 1F, 1F, 0.0F, 0.0F, Direction.UP, player);
    }
    protected float rotateTowards(float from, float to, float maxDelta) {
        float f = Mth.degreesDifference(from, to);
        float f1 = Mth.clamp(f, -maxDelta, maxDelta);
        return from + f1;
    }

    private void renderFace(T blockEntity, Matrix4f pose, VertexConsumer consumer, float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3, Direction direction) {

        consumer.addVertex(pose, x0, y0, z0);
        consumer.addVertex(pose, x1, y0, z1);
        consumer.addVertex(pose, x1, y1, z2);
        consumer.addVertex(pose, x0, y1, z3);
    }
    protected float getYRotD(float x, float z) {
        double d0 = x;
        double d1 = z;
        return (float) (Mth.atan2(d1, d0) * (double) 180.0F / (double) (float) Math.PI) - 90.0F;
    }
    protected float getOffsetUp() {
        return 0.75F;
    }

    protected float getOffsetDown() {
        return 0.375F;
    }
    protected RenderType renderType() {
        return Kirk.end_thingy();
    }

}