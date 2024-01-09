package org.me.lunarfarm.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class InventoryPrincipal  {

    private Player player;
    private CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));


    public InventoryPrincipal(Player player) throws IOException, InvalidConfigurationException, SQLException {
        this.player = player;
        createInventory();
    }
    private void createInventory() {
        Inventory inventory = Bukkit.createInventory(null, menus.getInt("farm.slots"), menus.getString("farm.name"));
        setItems(inventory);
    }

    private void setItems(Inventory inventory) {
        ConfigurationSection sections = menus.getConfigurationSection("farm.itens");
        for (String sec : menus.getConfigurationSection("farm.itens").getKeys(false)) {
            ItemStack item = new ItemStack(Material.getMaterial(sections.getInt(sec + ".id")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(sections.getString(sec + ChatColor.translateAlternateColorCodes('&', sec + ".name")));
            meta.setLore(translateColors(sections.getStringList( sec + ".lore")));
            item.setItemMeta(meta);
            inventory.setItem(sections.getInt(sec + ".inventory-slot"), item);
        }
        player.openInventory(inventory);
    }
    private List<String> translateColors(List<String> input){
        for (int i = 0; i < input.size(); i++) {
            input.set(i, ChatColor.translateAlternateColorCodes('&', input.get(i)));
        }
        return input;
    }

}
