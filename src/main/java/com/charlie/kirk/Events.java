package com.charlie.kirk;

import com.charlie.kirk.block.SiloBlockRenderer;
import com.charlie.kirk.data.TeleportBookRecord;
import com.charlie.kirk.datagen.*;
import com.charlie.kirk.entity.SahurMob;
import com.charlie.kirk.entity.SahurRenderer;
import com.charlie.kirk.menu.SiloContainerScreen;
import com.charlie.kirk.menu.SiloMenu;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.charlie.kirk.Kirk.*;
import static net.minecraft.client.renderer.RenderType.*;

@EventBusSubscriber(modid = "kirk")
public class Events {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Is this the tab we want to add to?
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(Kirk.COTTON.get());
            event.accept(RESIN.get());
            event.accept(CORE_WOOD.get());
        }else if(event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            event.accept(Kirk.BAT_ITEM.get());
        }else if(event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS)
        {
            event.accept(Kirk.GUNK_BLOCK_ITEM.get());
            event.accept(Kirk.GUNK_LOG_ITEM.get());
            event.accept(Kirk.GUNK_LEAVES_ITEM.get());
            event.accept(SWAMP_BRICKS_ITEM.get());
            event.accept(MOSSY_SWAMP_BRICKS_ITEM.get());
            event.accept(SEWER_DIRT_ITEM.get());
            event.accept(SEWER_FARMLAND_ITEM.get());
        }else if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
        {
            event.accept(Kirk.SILO_BLOCK_ITEM.get());
        }
    }
    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(Kirk.SILO_MENU.get(), SiloContainerScreen::new);
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(Kirk.SAHUR.get(), SahurRenderer::new);
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event)
    {
        event.put(Kirk.SAHUR.get(), SahurMob.createAttributes().build());
    }
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        event.addProvider(new KirkLanguageProvider(output));
        event.getGenerator().addProvider(
                event.includeClient(),
                new KirkBlockStateProvider(output, existingFileHelper)
        );
        event.getGenerator().addProvider(event.includeServer(), new KirkRecipeProvider(output, lookupProvider));
        event.addProvider(new KirkItemProvider(output, existingFileHelper));
    }
    @SubscribeEvent // on the mod event bus
    public static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<KirkLootTableProvider>) output -> new KirkLootTableProvider(
                        output,
                        // A set of required table resource locations. These are later verified to be present.
                        // It is generally not recommended for mods to validate existence,
                        // therefore we pass in an empty set.
                        Set.of(),
                        // A list of sub provider entries. See below for what values to use here.
                        List.of(), event.getLookupProvider()
                )
        );
        event.getGenerator().addProvider(event.includeServer(), new KirkGlobalLootModifierProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }
    @SubscribeEvent
    public static void onRegisterNamedRenderTypes(RegisterNamedRenderTypesEvent event)
    {
        //event.register(ResourceLocation.parse("special_cutout"), end_thingy(), Sheets.solidBlockSheet());
    }
    @SubscribeEvent // on the mod event bus
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        // Sets the component on melon seeds
        event.modify(Kirk.TELEPORT_BOOK, builder ->
                builder.set(TELEPORT_BOOK_DATA_COMPONENT.value(), new TeleportBookRecord(List.of()))
        );

        // Removes the component for any items that have a crafting item
        event.modifyMatching(
                item -> item.hasCraftingRemainingItem(),
                builder -> builder.remove(TELEPORT_BOOK_DATA_COMPONENT.value())
        );
    }
    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                // The block entity type to register the renderer for.
                SILO_BLOCK_ENTITY.get(),
                // A function of BlockEntityRendererProvider.Context to BlockEntityRenderer.
                SiloBlockRenderer::new
        );
    }
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), "rendertype_end_thingy", DefaultVertexFormat.POSITION), (Consumer)(p_172778_) -> rendertypeEndThingyShader = (ShaderInstance) p_172778_);
    }
}
