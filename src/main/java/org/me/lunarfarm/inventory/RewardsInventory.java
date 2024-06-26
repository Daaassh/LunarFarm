package org.me.lunarfarm.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;

import java.io.IOException;
import java.util.List;

public class RewardsInventory {
    private final Player p;
    private final CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));

    public RewardsInventory(Player p) throws IOException, InvalidConfigurationException {
        this.p = p;
        createInventory();
    }

    public void createInventory() {
        Inventory inventory = Bukkit.createInventory(null, rewards.getInt("rewards.inventory.slots"), ChatColor.translateAlternateColorCodes('&', rewards.getString("rewards.inventory.name")));
        setMenuItems(inventory);
    }
    private void setMenuItems(Inventory inventory) {
        for (String sec : rewards.getConfigurationSection("rewards.itens").getKeys(false)) {
            ItemStack item = new ItemStack(Material.getMaterial(rewards.getInt("rewards.itens." + sec + ".id")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',rewards.getString("rewards.itens." + sec + ".name")));
            meta.setLore(translateColors(rewards.getStringList("rewards.itens." + sec + ".lore")));
            item.setItemMeta(meta);
            inventory.setItem(rewards.getInt("rewards.itens." + sec + ".inventory.slot"), item);
        }
        p.openInventory(inventory);
    }
    private List<String> translateColors(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, ChatColor.translateAlternateColorCodes('&', input.get(i)));
        }
        return input;
    }
}
