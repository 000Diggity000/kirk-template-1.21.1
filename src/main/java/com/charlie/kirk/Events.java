package com.charlie.kirk;

import com.charlie.kirk.datagen.*;
import com.charlie.kirk.entity.SahurMob;
import com.charlie.kirk.entity.SahurRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = "kirk")
public class Events {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Is this the tab we want to add to?
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(Kirk.COTTON.get());
        }else if(event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            event.accept(Kirk.BAT_ITEM.get());
        }
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
    }
}
