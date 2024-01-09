package org.me.lunarfarm.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;
import org.me.lunarfarm.inventory.EnchantsInventory;
import org.me.lunarfarm.inventory.RewardsInventory;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.methods.Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
public class onInventoryClick implements Listener {
    private CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
    private CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
    private CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));

    public onInventoryClick() throws IOException, InvalidConfigurationException {
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) throws IOException, InvalidConfigurationException, SQLException {
        if (e.getClickedInventory().getName().equalsIgnoreCase(menus.getString("farm.name"))) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : null;
            if (clickedItem != null) {
                ConfigurationSection section = menus.getConfigurationSection("farm.itens");
                if (section != null) {
                    for (String itemName : section.getKeys(false)) {
                        try {
                            verifyItens(clickedItem, p, section, itemName, "farm");
                            verifyItens(clickedItem, p, section, itemName, "recompensas");
                            verifyItens(clickedItem, p, section, itemName, "enchants");
                        }catch (Exception ex) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm] verifique ce você trocou o nome das seções em menus.yml");
                        }
                    }
                  }
                }
                else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-created-itens-inventory")));
                    p.closeInventory();
                }
            }

        else if (e.getClickedInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',rewards.getString("rewards.inventory.name")))) {
            e.setCancelled(true);
        }
        else if (e.getClickedInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',menus.getString("enchants.name")))) {
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : null;
            Player p = (Player) e.getWhoClicked();
            if (clickedItem != null) {
                ConfigurationSection section = menus.getConfigurationSection("enchants.itens");
                if (section != null) {
                    for (String itemName : section.getKeys(false)) {
                        Methods.verifyItens(clickedItem, p, section, itemName, "fortuna");
                        Methods.verifyItens(clickedItem, p, section, itemName, "bonus");
                        Methods.verifyItens(clickedItem, p, section, itemName, "multiplicador");
                    }
                }
            }
            else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-created-itens-inventory")));
                p.closeInventory();
            }
        }
    }


    private void verifyItens(ItemStack clickedItem, Player p, ConfigurationSection section, String itemName, String util) throws SQLException, IOException, InvalidConfigurationException {
         if (Objects.equals(itemName, util)) {
            if (clickedItem.getType() == Material.getMaterial(section.getInt(itemName + ".id"))) {
                if (section.getBoolean(itemName + ".require-permission")) {
                    if (p.hasPermission(section.getString(itemName + ".permission"))) {
                        if (util.equalsIgnoreCase("enchants")) {
                            new EnchantsInventory(p);
                        }
                        else if(util.equalsIgnoreCase("recompensas")) {
                            new RewardsInventory(p);
                        }
                        else if(util.equalsIgnoreCase("farm")) {
                            new CreateHoe(MySqlUtils.getPlayer(p));
                            p.sendMessage(ChatColor.RED + "Você pegou a hoe");
                            p.closeInventory();
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
                    }
                }
                else {
                    if (util.equalsIgnoreCase("enchants")) {
                        new EnchantsInventory(p);
                    }
                    else if(util.equalsIgnoreCase("recompensas")) {
                        new RewardsInventory(p);
                    }
                    else if(util.equalsIgnoreCase("farm")) {
                        new CreateHoe(MySqlUtils.getPlayer(p));
                        p.sendMessage(ChatColor.RED + "Você pegou a hoe");
                        p.closeInventory();
                    }
                }
            }
        }
    }
}
