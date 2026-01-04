package com.charlie.kirk;

import com.google.common.collect.Sets;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Set;
@OnlyIn(Dist.CLIENT)
public class KirkModelLayers {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ModelLayerLocation SILO_BLOCK = register("silo_block");

    private static ModelLayerLocation register(String path) {
        return register(path, "main");
    }

    private static ModelLayerLocation register(String path, String model) {
        ModelLayerLocation modellayerlocation = createLocation(path, model);
        if (!ALL_MODELS.add(modellayerlocation)) {
            throw new IllegalStateException("Duplicate registration for " + String.valueOf(modellayerlocation));
        } else {
            return modellayerlocation;
        }
    }
    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kirk.MODID, path), model);
    }
}
