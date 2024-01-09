package org.me.lunarfarm.methods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.hoe.CreateHoe;
import org.me.lunarfarm.inventory.EnchantsInventory;
import org.me.lunarfarm.inventory.RewardsInventory;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Methods {
    private static FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private static  CustomFileConfiguration menus;

    static {
        try {
            menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static  CustomFileConfiguration msg;

    static {
        try {
            msg = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    public Methods() throws IOException, InvalidConfigurationException {
    }


    private static void addEnchants(String enchant, Player player) throws SQLException {
        PlayerManager manager = MySqlUtils.getPlayer(player);
        if (enchant.equalsIgnoreCase("fortuna")) {
            manager.setFortuna(manager.getFortuna() + 1);
        } else if (enchant.equalsIgnoreCase("bonus")) {
            manager.setBonus(manager.getBonus() + 1);
        } else if (enchant.equalsIgnoreCase("multiplicador")) {
            manager.setMultiplicador(manager.getMultiplicador() + 1);
        }
    }

    private static Integer getCostForEnchant(String enchant, Player player) throws SQLException {
        PlayerManager manager = MySqlUtils.getPlayer(player);
        if (enchant.equalsIgnoreCase("fortuna")) {
            int i = config.getInt("fortuna.initial-cost");
            return i + manager.getFortuna();
        } else if (enchant.equalsIgnoreCase("bonus")) {
            int i = config.getInt("bonus.initial-cost");
            return i + manager.getBonus();
        } else if (enchant.equalsIgnoreCase("multiplicador")) {
            int i = config.getInt("multiplicador.initial-cost");
            return i + manager.getMultiplicador();
        }
        return null;
    }

    public static void verifyItens(ItemStack clickedItem, Player p, ConfigurationSection section, String itemName, String util) throws SQLException, IOException, InvalidConfigurationException {
        PlayerManager manager = MySqlUtils.getPlayer(p);
        if (Objects.equals(itemName, util)) {
            if (clickedItem.getType() == Material.getMaterial(section.getInt(itemName + ".id"))) {
                if (util.equalsIgnoreCase("fortuna")) {
                    if (manager.getSeeds() >= getCostForEnchant("fortuna", p)) {
                        addEnchants("fortuna", p);
                    }
                    else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("no-seeds")));
                    }
                } else if (util.equalsIgnoreCase("bonus")) {
                    if (manager.getSeeds() >= getCostForEnchant("bonus", p)) {
                        addEnchants("bonus", p);
                    }
                    else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("no-seeds")));
                    }
                } else if (util.equalsIgnoreCase("multiplicador")) {
                    if (manager.getSeeds() >= getCostForEnchant("multiplicador", p)) {
                        addEnchants("multiplicador", p);
                    }
                    else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("no-seeds")));
                    }
                }
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("no-permission")));
            }
        }
    }
}
