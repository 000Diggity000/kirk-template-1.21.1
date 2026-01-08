package com.charlie.kirk.block;

import com.charlie.kirk.Kirk;
import com.charlie.kirk.menu.SiloMenu;
import com.mojang.datafixers.types.templates.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagType;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SiloBlockEntity extends RandomizableContainerBlockEntity {
    private final int SIZE = 54;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    public SiloBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, registries);
        }
    }
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, registries);
        }

    }

    public SiloBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Kirk.SILO_BLOCK_ENTITY.get(), blockPos, blockState);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("kirk.container.silo");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {

        if(!stack.is(ItemTags.FLOWERS))
        {
            return false;
        }
        return super.canPlaceItem(slot, stack);
    }

    @Override
    public void clearContent() {
        items.clear();
        this.setChanged();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return SiloMenu.sixRows(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }
}
