package com.charlie.kirk.menu;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SiloSlot extends Slot {

    public SiloSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(ItemTags.CHICKEN_FOOD) || stack.is(ItemTags.PIG_FOOD) || stack.is(ItemTags.HORSE_FOOD) || stack.is(ItemTags.SHEEP_FOOD) || stack.is(ItemTags.COW_FOOD);
    }
}
