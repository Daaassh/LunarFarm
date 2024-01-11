package org.me.lunarfarm.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.api.PrimeActionbar;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.enchants.economy.FortunaEnchant;
import org.me.lunarfarm.enchants.economy.MultiplicadorEnchant;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.porcentage.PorcentageManager;
import org.me.lunarfarm.rewards.CreateItems;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class onBreak implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) throws SQLException, IOException, InvalidConfigurationException {
        CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();


        Player p = e.getPlayer();
        if (e.getBlock().getType() == Material.CROPS || e.getBlock().getType() == Material.WHEAT) {
            if (p.hasPermission("lunarfarm.break_farms")) {
                if (p.getItemInHand().getType() == Material.WOOD_HOE) {
                    PlayerManager manager = MySqlUtils.getPlayer(p);
                    manager.setBlocks_break(manager.getBlocks_break() + 1);
                    if (config.getBoolean("break_title-api")) {
                        PrimeActionbar.sendActionbar(p, messages.getString("message-on-break-block").replace("@sementes", String.valueOf(manager.getSeeds())));
                    }
                    else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("message-on-break-block").replace("@sementes", String.valueOf(manager.getSeeds()))));
                    }
                    if (new PorcentageManager(rewards.getDouble("rewards.chance-initial")).setup()) {
                        new CreateItems(p);
                    }
                    new MultiplicadorEnchant(manager, 1);
                    new FortunaEnchant(manager, e.getBlock().getType());
                    p.getItemInHand().getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', hoe.getString("name").replace("@blocks_break", String.valueOf(MySqlUtils.getPlayer(p).getBlocks_break()))));
                    MySqlUtils.updatePlayer(manager, p);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getBlock().setType(Material.CROPS);
                        }
                    }.runTaskLater(Main.getPlugin(Main.class), 20L * config.getInt("break_farms.time-on-seconds-for-place-wheat"));
                } else {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-hoe")));
                }
            } else {
                e.setCancelled(true);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
            }
        }
    }
    private List<Material> getMaterials() throws IOException, InvalidConfigurationException {
        CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));
        List<Material> materialList = null;
        for (String sec : farms.getConfigurationSection("farms").getKeys(false)) {
            ConfigurationSection sections = farms.getConfigurationSection("farms");
            Material.getMaterial(sec);
            materialList.add(Material.getMaterial(sec));
        }
        return materialList;
    }
}
