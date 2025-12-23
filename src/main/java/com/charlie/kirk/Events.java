package com.charlie.kirk;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
@EventBusSubscriber(modid = "kirk")
public class Events {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Is this the tab we want to add to?
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(Kirk.COTTON.get());
        }
    }
}
