package com.charlie.kirk;

import com.charlie.kirk.block.AbstractSiloBlock;
import com.charlie.kirk.block.SiloBlock;
import com.charlie.kirk.block.SiloBlockEntity;
import com.charlie.kirk.block.SiloType;
import com.charlie.kirk.entity.SahurMob;
import com.charlie.kirk.item.BatItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
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

import java.util.function.Supplier;

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


    public static final DeferredBlock<SiloBlock> SILO_BLOCK =
            BLOCKS.register("silo_block", () -> new SiloBlock(BlockBehaviour.Properties.of().destroyTime(2.0f)));
    public static final DeferredItem<BlockItem> SILO_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "silo_block",
            SILO_BLOCK,
            new Item.Properties()
    );
    public static final EnumProperty<SiloType> SILO_TYPE = EnumProperty.create("type", SiloType.class);;
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
    public static final Supplier<MapCodec<? extends AbstractSiloBlock<SiloBlockEntity>>> SIMPLE_CODEC = CODECS.register(
            "silo_block",
            () -> BlockBehaviour.simpleCodec(SiloBlock::new)
    );

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
