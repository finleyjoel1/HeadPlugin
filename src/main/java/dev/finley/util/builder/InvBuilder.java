package dev.finley.util.builder;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvBuilder {

    private final String name;
    private final int size;
    private final List<ItemStack> items = new ArrayList<>();
    private Inventory inventory;

    //Size must be 27 or 45

    public InvBuilder(String name, int size) {
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, this.size, ChatColor.translateAlternateColorCodes('&', this.name));
    }

    public InvBuilder(InventoryView inventoryView) {
        this.name = inventoryView.getTitle();
        this.size = inventoryView.getTopInventory().getSize();
    }

    public InvBuilder addItem(ItemStack itemStack) {
        this.items.add(itemStack);
        return this;
    }

    public InvBuilder setItemInMiddle(ItemStack itemStack) {
        int start = 0;
        int end = 45;
        int middle = (start + end) / 2;
        this.inventory.setItem(middle, itemStack);
        return this;
    }

    public InvBuilder setItemToAll(ItemStack itemStack) {
        for (int i = 0; i < size; i++) {
            this.inventory.setItem(i, itemStack);
        }
        return this;
    }

    public void setItemToAll(Inventory inventory, ItemStack itemStack) {
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, itemStack);
        }
    }

    public InvBuilder setItem(ItemStack itemStack, int slot) {
        this.inventory.setItem(slot, itemStack);
        return this;
    }

    public InvBuilder setItems(List<ItemStack> itemStacks) {
        int count = 0;
        for (ItemStack itm : itemStacks) {
            this.inventory.setItem(count, itm);
            count++;
        }
        return this;
    }

    public InvBuilder addItems(List<ItemStack> itemStacks) {
        this.items.addAll(itemStacks);
        return this;
    }

    public Inventory build() {
        this.inventory.setMaxStackSize(this.size);
        this.items.forEach(itemStack -> inventory.addItem(itemStack));
        return this.inventory;
    }
}
