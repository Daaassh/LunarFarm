package org.me.lunarfarm.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;

import java.io.IOException;
import java.sql.SQLException;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        try {
            MySqlUtils.savePlayer(e.getPlayer());
            new CreateHoe(MySqlUtils.getPlayer(e.getPlayer()));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Player " + e.getPlayer().getName() + " adicionado ao banco de dados");
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
