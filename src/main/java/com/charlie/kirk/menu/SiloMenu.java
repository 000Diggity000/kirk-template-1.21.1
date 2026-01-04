package com.charlie.kirk.menu;

import com.charlie.kirk.Kirk;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SiloMenu extends AbstractContainerMenu {
    private static final int SLOTS_PER_ROW = 9;
    private final Container container;
    private final int containerRows;

    private SiloMenu(MenuType<?> type, int containerId, Inventory playerInventory, int rows) {
        this(type, containerId, playerInventory, new SimpleContainer(9 * rows), rows);
    }

    public static SiloMenu oneRow(int containerId, Inventory playerInventory) {
        return new SiloMenu(MenuType.GENERIC_9x1, containerId, playerInventory, 1);
    }

    public static SiloMenu twoRows(int containerId, Inventory playerInventory) {
        return new SiloMenu(MenuType.GENERIC_9x2, containerId, playerInventory, 2);
    }

    public static SiloMenu threeRows(int containerId, Inventory playerInventory) {
        return new SiloMenu(MenuType.GENERIC_9x3, containerId, playerInventory, 3);
    }

    public static SiloMenu fourRows(int containerId, Inventory playerInventory) {
        return new SiloMenu(MenuType.GENERIC_9x4, containerId, playerInventory, 4);
    }

    public static SiloMenu fiveRows(int containerId, Inventory playerInventory) {
        return new SiloMenu(MenuType.GENERIC_9x5, containerId, playerInventory, 5);
    }

    public static SiloMenu sixRows(int containerId, Inventory playerInventory) {
        return new SiloMenu(MenuType.GENERIC_9x6, containerId, playerInventory, 6);
    }

    public static SiloMenu threeRows(int containerId, Inventory playerInventory, Container container) {
        return new SiloMenu(MenuType.GENERIC_9x3, containerId, playerInventory, container, 3);
    }

    public static SiloMenu sixRows(int containerId, Inventory playerInventory, Container container) {
        return new SiloMenu(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6);
    }

    public SiloMenu(MenuType<?> type, int containerId, Inventory playerInventory, Container container, int rows) {
        super(type, containerId);
        checkContainerSize(container, rows * 9);
        this.container = container;
        this.containerRows = rows;
        container.startOpen(playerInventory.player);
        int i = (this.containerRows - 4) * 18;

        for(int j = 0; j < this.containerRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(container, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }

    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.containerRows * 9) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
    public Container getContainer() {
        return this.container;
    }

    public int getRowCount() {
        return this.containerRows;
    }
}
