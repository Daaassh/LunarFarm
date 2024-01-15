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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.api.TitleAPI;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;
import org.me.lunarfarm.inventory.AreasInventory;
import org.me.lunarfarm.inventory.EnchantsInventory;
import org.me.lunarfarm.inventory.RewardsInventory;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.methods.Methods;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class onInventoryClick implements Listener {
    TitleAPI api = new TitleAPI();


    @EventHandler
    public void onClick(InventoryClickEvent e) throws IOException, InvalidConfigurationException, SQLException {
        PlayerManager manager = PlayersInFarm.cache.getIfPresent(e.getWhoClicked().getUniqueId());
        if (!(manager == null)) {
            CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
            CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
            if (e.getClickedInventory().getName().equalsIgnoreCase(menus.getString("farm.name"))) {
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();
                ItemStack clickedItem = e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : null;
                if (clickedItem != null) {
                    ConfigurationSection section = menus.getConfigurationSection("farm.itens");
                    if (section != null) {
                        for (String itemName : section.getKeys(false)) {
                            try {
                                verifyItens(clickedItem, p, section, itemName, "areas");
                                verifyItens(clickedItem, p, section, itemName, "rewards");
                            } catch (Exception ex) {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm] verifique ce você trocou o nome das seções em menus.yml");
                            }
                        }
                    }
                }
            } else if (e.getClickedInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', rewards.getString("rewards.inventory.name")))) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ Lunar Farm ] " + ChatColor.GREEN + "Aqui vai o inventário dos itens de recompensas");
                e.setCancelled(true);
            }
        }

    }

    private void verifyItens (ItemStack clickedItem, Player p, ConfigurationSection section, String itemName, String util) throws SQLException, IOException, InvalidConfigurationException {
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        if (Objects.equals(itemName, util)) {
            if (clickedItem.getType() == Material.getMaterial(section.getInt(itemName + ".id"))) {
                if (section.getBoolean(itemName + ".require-permission")) {
                    if (p.hasPermission(section.getString(itemName + ".permission"))) {
                        if (util.equalsIgnoreCase("rewards")) {
                            try {
                                new RewardsInventory(p);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (util.equalsIgnoreCase("areas")) {
                            if (PlayersInFarm.playersInFarm.containsKey(p.getUniqueId())) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("player-in-farm")));
                            }
                            else {
                                new AreasInventory(MySqlUtils.getPlayer(p));
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
                    }
                } else {
                    if (util.equalsIgnoreCase("rewards")) {
                        try {
                            new RewardsInventory(p);
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (util.equalsIgnoreCase("areas")) {
                        new CreateHoe(MySqlUtils.getPlayer(p));
                        p.sendMessage(ChatColor.RED + "Você pegou a hoe");
                        p.closeInventory();
                    }
                }
            }
        }
    }
}

