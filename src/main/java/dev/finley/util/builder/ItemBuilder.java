package dev.finley.util.builder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.finley.util.Printer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        if (name.isBlank()) return this;
        this.itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemBuilder setSize(int size) {
        if (itemStack == null) return this;
        this.itemStack.setAmount(size);
        return this;
    }

    public ItemBuilder setLore(String lore) {
        if (lore.isBlank()) return this;
        this.itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', lore)));
        return this;
    }

    public ItemBuilder setEmptyName(String colorCode) {
        if (colorCode.isBlank()) return this;
        this.itemMeta.setDisplayName("§" + colorCode);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setHead(String base64) {
        if (this.itemMeta == null || !(this.itemMeta instanceof SkullMeta headMeta)) return this;

        UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createPlayerProfile(uuid);
        PlayerTextures textures = profile.getTextures();

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            String decodedString = new String(decodedBytes);
            JsonObject json = JsonParser.parseString(decodedString).getAsJsonObject();
            String url = json.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            URL skinUrl = new URL(url);
            textures.setSkin(skinUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }

        profile.setTextures(textures);
        headMeta.setOwnerProfile(profile);
        this.itemMeta = headMeta;
        return this;
    }

    public ItemBuilder setUserNameHead(String username) {
        if (this.itemMeta == null) return this;
        if (!(this.itemMeta instanceof SkullMeta headMeta)) return this;
        try {
            PlayerProfile profile = Bukkit.createPlayerProfile(username);
            headMeta.setOwnerProfile(profile);
            this.itemMeta = headMeta;
        } catch (Exception e) {
            Printer.sendMessage("Can't find player name: " + username);
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore.isEmpty()) return this;
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemStack build() {
        if (itemStack == null || itemMeta == null) return null;
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
