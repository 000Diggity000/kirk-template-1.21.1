package com.charlie.kirk;

import com.charlie.kirk.block.*;
import com.charlie.kirk.data.TeleportBookRecord;
import com.charlie.kirk.data.TeleportPosition;
import com.charlie.kirk.entity.SahurMob;
import com.charlie.kirk.item.BatItem;
import com.charlie.kirk.item.TeleportBookItem;
import com.charlie.kirk.menu.SiloMenu;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.http.impl.io.AbstractSessionInputBuffer;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Kirk.MODID)
public class Kirk {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "kirk";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceKey<LootTable> RESIN_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Kirk.MODID, "resin"));
    public static final ResourceKey<LootTable> CORE_WOOD_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Kirk.MODID, "core_wood"));;
    public static final Codec<TeleportBookRecord> TELEPORT_BOOK_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TeleportPosition.CODEC.sizeLimitedListOf(256).optionalFieldOf("positions", List.of()).forGetter(TeleportBookRecord::positions)
            ).apply(instance,TeleportBookRecord::new)
    );
    public static final StreamCodec<ByteBuf, TeleportBookRecord> TELEPORT_BOOK_STREAM_CODEC = StreamCodec.composite(
            TeleportPosition.TELEPORT_POSITION_STREAM_CODEC.apply(ByteBufCodecs.list(256)), TeleportBookRecord::positions, TeleportBookRecord::new
    );
    @Nullable
    public static ShaderInstance rendertypeEndThingyShader;
    @Nullable
    public static ShaderInstance getRendertypeEndThingyShader() {
        return rendertypeEndThingyShader;
    }
    public static final ShaderStateShard RENDERTYPE_END_THINGY_SHADER = new ShaderStateShard(Kirk::getRendertypeEndThingyShader);
    public static RenderType END_GATEWAY = RenderType.create("end_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 1536, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_END_THINGY_SHADER).setTextureState(MultiTextureStateShard.builder().add(TheEndPortalRenderer.END_SKY_LOCATION, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, true).build()).createCompositeState(true));
    public static RenderType end_thingy() {
        return END_GATEWAY;
    }
    // Create a Deferred Register to hold Blocks which will all be registered under the "kirk" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "kirk" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final Supplier<MenuType<SiloMenu>> SILO_MENU = MENUS.register("silo_menu", () -> new MenuType<SiloMenu>(SiloMenu::sixRows, DEFAULT_FLAGS));
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TeleportBookRecord>> TELEPORT_BOOK_DATA_COMPONENT = DATA_COMPONENTS.registerComponentType(
            "teleport_book_data_component",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(TELEPORT_BOOK_CODEC)
                    // The codec to read/write the data across the network
                    .networkSynchronized(TELEPORT_BOOK_STREAM_CODEC)
    );
    public static final DeferredBlock<SiloBlock> SILO_BLOCK =
            BLOCKS.register("silo_block", () -> new SiloBlock(BlockBehaviour.Properties.of().destroyTime(2.0f).noOcclusion()));
    public static final DeferredBlock<LogBlock> LOG_BLOCK =
            BLOCKS.register("log_block", () -> new LogBlock(BlockBehaviour.Properties.of().mapColor((p_152624_) -> p_152624_.getValue(LogBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<Block> GUNK_BLOCK =
            BLOCKS.register("gunk_block", () -> new Block(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.MUD)));
    public static final DeferredBlock<Block> SWAMP_BRICKS_BLOCK =
            BLOCKS.register("swamp_bricks_block", () -> new Block(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_GREEN).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MOSSY_SWAMP_BRICKS_BLOCK =
            BLOCKS.register("mossy_swamp_bricks_block", () -> new Block(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_GREEN).sound(SoundType.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> GUNK_LOG = BLOCKS.register("gunk_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_MAGENTA).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<LeavesBlock> GUNK_LEAVES = BLOCKS.register("gunk_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).randomTicks().noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> SEWER_DIRT =
            BLOCKS.register("sewer_dirt", () -> new Block(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.GRASS).noOcclusion()));
    public static final DeferredBlock<Block> SEWER_FARMLAND =
            BLOCKS.register("sewer_farmland", () -> new SewerFarmland(BlockBehaviour.Properties.of().destroyTime(2.0f).mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.GRASS)));
    public static final DeferredItem<BlockItem> SEWER_DIRT_ITEM = ITEMS.registerSimpleBlockItem(
            "sewer_dirt_item",
            SEWER_DIRT,
            new Item.Properties()
    );public static final DeferredItem<BlockItem> SEWER_FARMLAND_ITEM = ITEMS.registerSimpleBlockItem(
            "sewer_farmland_item",
            SEWER_FARMLAND,
            new Item.Properties()
    );

    public static final DeferredItem<BlockItem> SILO_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "silo_block",
            SILO_BLOCK,
            new Item.Properties()
    );
    public static final DeferredItem<BlockItem> SWAMP_BRICKS_ITEM = ITEMS.registerSimpleBlockItem(
            "swamp_bricks",
            SWAMP_BRICKS_BLOCK,
            new Item.Properties()
    );
    public static final DeferredItem<BlockItem> MOSSY_SWAMP_BRICKS_ITEM = ITEMS.registerSimpleBlockItem(
            "mossy_swamp_bricks",
            MOSSY_SWAMP_BRICKS_BLOCK,
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
    public static final DeferredItem<Item> RESIN = ITEMS.registerItem(
            "resin",
            Item::new, // The factory that the properties will be passed into.
            new Item.Properties() // The properties to use.
    );
    public static final DeferredItem<Item> CORE_WOOD = ITEMS.registerItem(
            "core_wood",
            Item::new, // The factory that the properties will be passed into.
            new Item.Properties() // The properties to use.
    );

    public static final DeferredItem<TeleportBookItem> TELEPORT_BOOK = ITEMS.registerItem(
            "teleport_book",
            TeleportBookItem::new, // The factory that the properties will be passed into.
            new Item.Properties()//.component(TELEPORT_BOOK_DATA_COMPONENT.value(), new TeleportBookRecord("",0,0,0)) // The properties to use.
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
    public static final Supplier<EntityType<SahurMob>> SAHUR = ENTITY_TYPES.register("sahur", () -> EntityType.Builder.of(SahurMob::new, MobCategory.MONSTER).sized(1f, 1f).setTrackingRange(10).build("sahur"));
    public static final DeferredRegister<MapCodec<? extends Block>> CODECS = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, MODID);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Kirk(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        DATA_COMPONENTS.register(modEventBus);
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
