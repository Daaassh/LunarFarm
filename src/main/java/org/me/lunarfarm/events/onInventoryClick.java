package org.me.lunarfarm.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;
import org.me.lunarfarm.inventory.RewardsInventory;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.methods.Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
public class onInventoryClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) throws IOException, InvalidConfigurationException, SQLException {
        CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
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
                            verifyItens(clickedItem, p, section, itemName, "recompensas");
                        } catch (Exception ex) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm] verifique ce você trocou o nome das seções em menus.yml");
                        }
                    }
                }
            }
        } else if (e.getClickedInventory().getName().equalsIgnoreCase(rewards.getString("rewards.inventory.name"))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ Lunar Farm ] " + ChatColor.GREEN + "Aqui vai o inventário dos itens de recompensas");
            e.setCancelled(true);
        } else if (e.getClickedInventory().getName().equalsIgnoreCase(menus.getString("enchants.name"))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ Lunar Farm ] " + ChatColor.GREEN + "Aqui vai o inventário dos itens do encantamento");
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : null;
            Player p = (Player) e.getWhoClicked();
            if (clickedItem != null) {
                ConfigurationSection section = menus.getConfigurationSection("enchants.itens");
                if (section != null) {
                    for (String itemName : section.getKeys(false)) {
                        if (clickedItem.getType() == Material.getMaterial(section.getInt(itemName + ".id"))) {
                            verifyEnchantsItens(itemName, e.getAction(), p);
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-created-itens-inventory")));
                    p.closeInventory();
                }
            }
        }
        else {
            return;
        }
    }

    private void verifyEnchantsItens (String itemName, InventoryAction action, Player p) throws SQLException, IOException, InvalidConfigurationException {
       CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
       CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
       CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        if (itemName == "fortuna") {
            PlayerManager manager = MySqlUtils.getPlayer(p);
            if (action == InventoryAction.PICKUP_ONE) {
                if (Methods.getCostForEnchant("fortuna", p) >= manager.getSeeds()) {
                    Methods.addEnchants("fortuna", p);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-seeds")));
                }
            }
        }
        else if (itemName == "bonus") {
            PlayerManager manager = MySqlUtils.getPlayer(p);
            if (action == InventoryAction.PICKUP_ONE) {
                if (Methods.getCostForEnchant("bonus", p) >= manager.getSeeds()) {
                    Methods.addEnchants("bonus", p);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-seeds")));
                }
            }
        }
        else if (itemName == "multiplicador") {
            PlayerManager manager = MySqlUtils.getPlayer(p);
            if (action == InventoryAction.PICKUP_ONE) {
                if (Methods.getCostForEnchant("multiplicador", p) >= manager.getSeeds()) {
                    Methods.addEnchants("multiplicador", p);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-seeds")));
                }
            }
        }
    }
    private void verifyItens (ItemStack clickedItem, Player p, ConfigurationSection section, String itemName, String util) throws SQLException, IOException, InvalidConfigurationException {
        CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        if (Objects.equals(itemName, util)) {
            if (clickedItem.getType() == Material.getMaterial(section.getInt(itemName + ".id"))) {
                if (section.getBoolean(itemName + ".require-permission")) {
                    if (p.hasPermission(section.getString(itemName + ".permission"))) {
                        if (util.equalsIgnoreCase("recompensas")) {
                            new RewardsInventory(p);
                        } else if (util.equalsIgnoreCase("areas")) {
                            new CreateHoe(MySqlUtils.getPlayer(p));
                            p.sendMessage(ChatColor.RED + "Você pegou a hoe");
                            p.closeInventory();
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
                    }
                } else {
                    if (util.equalsIgnoreCase("recompensas")) {
                        new RewardsInventory(p);
                    } else if (util.equalsIgnoreCase("farm")) {
                        new CreateHoe(MySqlUtils.getPlayer(p));
                        p.sendMessage(ChatColor.RED + "Você pegou a hoe");
                        p.closeInventory();
                    }
                }
            }
        }
    }
}

