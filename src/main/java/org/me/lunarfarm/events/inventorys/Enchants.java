package org.me.lunarfarm.events.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.api.TitleAPI;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.inventory.EnchantsInventory;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.methods.Methods;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Objects;

public class Enchants implements Listener {
    TitleAPI api = new TitleAPI();
    CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));

    public Enchants() throws IOException, InvalidConfigurationException {
    }

    @EventHandler
    public void enchantsInteractEvent(InventoryClickEvent e) throws SQLException, IOException, InvalidConfigurationException {
        if (e.getClickedInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', menus.getString("enchants.name")))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ Lunar Farm ] " + ChatColor.GREEN + "Aqui vai o inventário dos itens do encantamento");
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : null;
            Player p = (Player) e.getWhoClicked();
            if (clickedItem != null) {
                ConfigurationSection section = menus.getConfigurationSection("enchants.itens");
                if (section != null) {
                    verifyEnchantsItems(e.getAction(), p, clickedItem);
                }
            }
        }
    }
    private void verifyEnchantsItems(InventoryAction action, Player p, ItemStack clickedITem) throws IOException, InvalidConfigurationException, SQLException {
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        PlayerManager manager = PlayersInFarm.cache.getIfPresent(p.getUniqueId());
        ConfigurationSection sections = menus.getConfigurationSection("enchants.itens");

        for (String section : menus.getConfigurationSection("enchants.itens").getKeys(false)) {
            Material material = Material.getMaterial(sections.getInt(section + ".id"));
            if (action == InventoryAction.PICKUP_ONE) {
                if (!(material == null)) {
                    if (clickedITem.getType() == material) {
                        if (Methods.getCostForEnchant(section, p) >= manager.getSeeds()) {
                            Methods.addEnchants(section, p);
                            Integer fadeIn = config.getInt(section + "on-level-up.fadeIn");
                            Integer stay = config.getInt(section + "on-level-up.stay");
                            Integer fadeOut = config.getInt(section + "on-level-up.fadeOut");
                            api.sendFullTitle(p, fadeIn, stay, fadeOut, config.getString(section + "on-level-up.title"), config.getString(section + "on-level-up.subtitle"));
                            p.closeInventory();
                            new EnchantsInventory(p);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-seeds")));
                        }
                    }
                }
            }
        }
    }
}
