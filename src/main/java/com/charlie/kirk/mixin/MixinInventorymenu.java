package com.charlie.kirk.mixin;

import com.charlie.kirk.Kirk;
import com.charlie.kirk.menu.KirkInventory;
import com.charlie.kirk.menu.TeleportBookScreen;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.level.NoteBlockEvent;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(LocalPlayer.class)
public abstract class MixinInventorymenu{
    @Shadow
    protected final Minecraft minecraft;
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    protected MixinInventorymenu(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void openItemGui(ItemStack stack, InteractionHand hand) {
        if (stack.is(Items.WRITABLE_BOOK)) {
            this.minecraft.setScreen(new BookEditScreen((Player)(Object)this, stack, hand));
        }else if(stack.is(Kirk.TELEPORT_BOOK.get()))
        {
           this.minecraft.setScreen(new TeleportBookScreen(Component.translatable("kirk.screen.nigger_screen"), stack, hand));
        }

    }

}
