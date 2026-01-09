package com.charlie.kirk;

import com.charlie.kirk.block.AbstractSiloBlock;
import com.charlie.kirk.block.SiloBlock;
import com.charlie.kirk.block.SiloBlockEntity;
import com.charlie.kirk.entity.SahurMob;
import com.charlie.kirk.item.BatItem;
import com.charlie.kirk.menu.SiloMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.http.impl.io.AbstractSessionInputBuffer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Kirk.MODID)
public class Kirk {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "kirk";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "kirk" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "kirk" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final Supplier<MenuType<SiloMenu>> SILO_MENU = MENUS.register("silo_menu", () -> new MenuType<SiloMenu>(SiloMenu::sixRows, DEFAULT_FLAGS));
    public static final DeferredBlock<SiloBlock> SILO_BLOCK =
            BLOCKS.register("silo_block", () -> new SiloBlock(BlockBehaviour.Properties.of().destroyTime(2.0f)));
    public static final DeferredBlock<Block> GUNK_BLOCK =
            BLOCKS.register("gunk_block", () -> new Block(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.MUD)));
    public static final DeferredBlock<RotatedPillarBlock> GUNK_LOG = BLOCKS.register("gunk_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<LeavesBlock> GUNK_LEAVES = BLOCKS.register("gunk_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).randomTicks().noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)));
    public static final DeferredItem<BlockItem> SILO_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "silo_block",
            SILO_BLOCK,
            new Item.Properties()
    );
    public static final DeferredItem<BlockItem> GUNK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "gunk_block",
            GUNK_BLOCK,
            new Item.Properties()
    );
    public static final DeferredItem<BlockItem> GUNK_LOG_ITEM = ITEMS.registerSimpleBlockItem(
            "gunk_log",
            GUNK_LOG,
            new Item.Properties()
    );
    public static final DeferredItem<BlockItem> GUNK_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(
            "gunk_leaves",
            GUNK_LEAVES,
            new Item.Properties()
    );

    public static final ResourceLocation BASE_KNOCKBACK_ID = ResourceLocation.withDefaultNamespace("base_knockback");
    public static final DeferredItem<Item> COTTON = ITEMS.registerItem(
            "cotton",
            Item::new, // The factory that the properties will be passed into.
            new Item.Properties() // The properties to use.
    );
    public static final Supplier<BlockEntityType<SiloBlockEntity>> SILO_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "silo_block_entity",
            // The block entity type, created using a builder.
            () -> BlockEntityType.Builder.of(
                            // The supplier to use for constructing the block entity instances.
                            SiloBlockEntity::new,
                            // A vararg of blocks that can have this block entity.
                            // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                            SILO_BLOCK.get()
                    )
                    // Build using null; vanilla does some datafixer shenanigans with the parameter that we don't need.
                    .build(null)
    );
    public static final DeferredItem<BatItem> BAT_ITEM = ITEMS.register("bat_item", () -> new BatItem(Tiers.WOOD, (new Item.Properties()).attributes(BatItem.createAttributesBat(Tiers.WOOD, 3, -2.4F, 1f))));
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
    public static final Supplier<EntityType<SahurMob>> SAHUR = ENTITY_TYPES.register("sahur", () -> EntityType.Builder.of(SahurMob::new, MobCategory.MONSTER).sized(1f, 3f).setTrackingRange(10).build("sahur"));
    public static final DeferredRegister<MapCodec<? extends Block>> CODECS = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, MODID);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Kirk(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CODECS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Kirk) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

}
