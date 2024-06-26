package org.me.lunarfarm.methods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Methods {
    private static FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    static {
        try {
            CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
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


    public static void addEnchants(String enchant, Player player) throws SQLException {
        PlayerManager manager = PlayersInFarm.cache.getIfPresent(player.getUniqueId());
        if (enchant.equalsIgnoreCase("fortuna")) {
            manager.setFortuna(manager.getFortuna() + 1);
        } else if (enchant.equalsIgnoreCase("bonus")) {
            manager.setBonus(manager.getBonus() + 1);
        } else if (enchant.equalsIgnoreCase("multiplicador")) {
            manager.setMultiplicador(manager.getMultiplicador() + 1);
        }
    }

    public static Integer getCostForEnchant(String enchant, Player player) throws SQLException {
        PlayerManager manager = PlayersInFarm.cache.getIfPresent(player.getUniqueId());
        int i = 0;
        if (enchant.equalsIgnoreCase("fortuna")) {
            i = config.getInt("fortune.initial-cost");
            i = i + manager.getFortuna();
        } else if (enchant.equalsIgnoreCase("bonus")) {
            i = config.getInt("bonus.initial-cost");
            i = i + manager.getBonus();
        } else if (enchant.equalsIgnoreCase("multiplicador")) {
            i = config.getInt("multiplicador.initial-cost");
            i = i + manager.getMultiplicador();
        }
        return i;
    }

    public static ItemStack getMaterial(ItemStack item, ConfigurationSection section, String itemName) {
        if (item.getType() == Material.getMaterial(section.getInt(itemName + ".id"))) {
            return item;
        }
        return null;
    }
    public static void verifyItens(ItemStack clickedItem, Player p, ConfigurationSection section, String itemName, String util) throws SQLException, IOException, InvalidConfigurationException {
        PlayerManager manager = PlayersInFarm.cache.getIfPresent(p.getUniqueId());
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
