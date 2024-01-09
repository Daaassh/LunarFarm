package org.me.lunarfarm.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EnchantsInventory {
    private Player player;
    private CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
    private FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    public EnchantsInventory(Player player) throws IOException, InvalidConfigurationException, SQLException {
        this.player = player;
        createInventory();
    }
    private void createInventory() throws SQLException {
        Inventory inventory = Bukkit.createInventory(null, menus.getInt("enchants.slots"), menus.getString("enchants.name"));
        setItems(inventory);
    }
    private void setItems(Inventory inventory) throws SQLException {
        ConfigurationSection sections = menus.getConfigurationSection("enchants.itens");
        for (String sec : menus.getConfigurationSection("enchants.itens").getKeys(false)) {
            ItemStack item = new ItemStack(Material.getMaterial(sections.getInt(sec + ".id")));
            ItemMeta meta = item.getItemMeta();
            if (sec.equalsIgnoreCase("fortuna")) {
                meta.setDisplayName(sections.getString(sec + ChatColor.translateAlternateColorCodes('&', sec + ".name").replace("@nivel", String.valueOf(getEnchant("fortuna", player)).replace("@max_level", String.valueOf(getMaxLevel("fortuna"))))));
            }
            else if(sec.equalsIgnoreCase("bonus")) {
                meta.setDisplayName(sections.getString(sec + ChatColor.translateAlternateColorCodes('&', sec + ".name").replace("@nivel", String.valueOf(getEnchant("bonus", player))).replace("@max_level", String.valueOf(getMaxLevel("bonus")))));
            }
            else if(sec.equalsIgnoreCase("multiplicador")) {
                meta.setDisplayName(sections.getString(sec + ChatColor.translateAlternateColorCodes('&', sec + ".name").replace("@nivel", String.valueOf(getEnchant("multiplicador", player)).replace("@max_level", String.valueOf(getMaxLevel("multiplicador"))))));
            }
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
    private Integer getEnchant(String enchant, Player p) throws SQLException {
        PlayerManager manager = MySqlUtils.getPlayer(p);
        if (enchant.equalsIgnoreCase("fortuna")) {
            return manager.getFortuna();
        }
        else if(enchant.equalsIgnoreCase("bonus")) {
            return manager.getBonus();
        }
        else if(enchant.equalsIgnoreCase("multiplicador")) {
            return manager.getMultiplicador();
        }
        return null;
    }
    private Integer getMaxLevel(String enchant) {
        Integer maxLevel = null;
        if (enchant.equalsIgnoreCase("fortuna")) {
            maxLevel = config.getInt("fortuna.max-level");
        }
        else if(enchant.equalsIgnoreCase("bonus")) {
            maxLevel = config.getInt("bonus.max-level");
        }
        else if(enchant.equalsIgnoreCase("multiplicador")) {
            maxLevel = config.getInt("multiplicador.max-level");
        }
        return maxLevel;
    }
}
