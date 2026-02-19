package com.charlie.kirk.menu;

import com.charlie.kirk.mixin.MixinInventorymenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

public class KirkInventory extends InventoryMenu {
    public KirkInventory(Inventory playerInventory, boolean active, Player owner) {
        super(playerInventory, active, owner);
        this.addSlot(new Slot(playerInventory, 41, 10, 10));
    }

}
