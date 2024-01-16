package org.me.lunarfarm.methods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;
import java.sql.SQLException;

public class TaskForUpdatePlayers {
    static FileConfiguration configuration = Main.getPlugin(Main.class).getConfig();
    static CustomFileConfiguration msg;

    static {
        try {
            msg = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        } catch (IOException  | InvalidConfigurationException e)  {
            throw new RuntimeException(e);
        }
    }

    static Integer i = 0;
    public static void runTask(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player ps : Bukkit.getOnlinePlayers()) {
                    if (!(PlayersInFarm.cache.getIfPresent(ps.getUniqueId()) == null)) {
                        PlayerManager manager = PlayersInFarm.cache.getIfPresent(ps.getUniqueId());
                        assert manager != null;
                        MySqlUtils.updatePlayer(manager, ps);
                        PlayersInFarm.cache.invalidate(ps.getUniqueId());
                        i++;
                        try {
                            PlayersInFarm.cache.put(ps.getUniqueId(), MySqlUtils.getPlayer(ps));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("uṕdate-players")).replace("@jogadores", i.toString()));
                i = 0;
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L* configuration.getLong("break_farms.update-acount"), configuration.getInt("break_farms.period"));
    }
}
