package org.me.lunarfarm.events.inventorys;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;

import java.io.IOException;

public class Areas implements Listener {
    private final CustomFileConfiguration areas = new CustomFileConfiguration("areas.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));

    public Areas() throws IOException, InvalidConfigurationException {

    }


    @EventHandler
    public void onAreaClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', areas.getString("areas.inventory.name")))) {
            ItemStack clickedItem = e.getCurrentItem();
            e.setCancelled(true);
            getItems(clickedItem, p);
        }
    }

    private void getItems(ItemStack clickedItem, Player player) {
        for (String sec : areas.getConfigurationSection("areas").getKeys(false)) {
            Bukkit.getConsoleSender().sendMessage("Iniciando for: " + clickedItem);
            ConfigurationSection section = areas.getConfigurationSection("areas");
            Material material = Material.getMaterial(section.getInt(sec + ".inventory.id"));
            if (!(material == null)) {
                ItemStack item = new ItemStack(material);
                if (item.getType() == clickedItem.getType()) {
                    try {
                        double x = section.getDouble(sec + ".location.x");
                        double y = section.getDouble(sec + ".location.y");
                        double z = section.getDouble(sec + ".location.z");
                        float yaw = (float) section.getDouble(sec + ".location.yaw");
                        float pitch = (float) section.getDouble(sec + ".location.pitch");
                        World world = Bukkit.getWorld(section.getString(sec + ".location.world"));
                        Location loc = new Location(world, x, y, z, yaw, pitch);
                        new CreateHoe(PlayersInFarm.cache.getIfPresent(player.getUniqueId()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("correct-send-player-to-area")));
                        player.teleport(loc);
                        PlayersInFarm.playersInFarm.put(player.getUniqueId(), player);
                    }
                    catch (Exception e) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("error-to-send-player-to-area")));
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

