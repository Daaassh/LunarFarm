package org.me.lunarfarm.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;

import java.io.IOException;

public class onDropItem implements Listener {
    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private final CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));

    public onDropItem() throws IOException, InvalidConfigurationException {
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase(config.getString("break_farms.world"))) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-drop-item")));
            e.setCancelled(true);
        }
    }
}
