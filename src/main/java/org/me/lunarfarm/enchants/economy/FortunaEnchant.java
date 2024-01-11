package org.me.lunarfarm.enchants.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;

public class FortunaEnchant {
    private PlayerManager manager;
    private Material item;
    private FileConfiguration configuration = Main.getPlugin(Main.class).getConfig();
    private  CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));


    public FortunaEnchant(PlayerManager manager, Material item) throws IOException, InvalidConfigurationException {
        this.manager = manager;
        this.item = item;
        setup();
    }
    private void setup(){
        Double coins = manager.getFortuna() * configuration.getDouble("fortune.increase-for-level");
        for (String sec : farms.getConfigurationSection("farms").getKeys(false)) {
            ConfigurationSection sections = farms.getConfigurationSection("farms");
            Material material = Material.getMaterial(sec);
            if (material == item) {
                Double coin = coins + farms.getDouble("farms." + sec + ".coins");
                manager.setSeeds(manager.getSeeds() + farms.getInt("farms." + sec + ".seeds"));
                manager.getPlayer().sendMessage("Coins ao quebrar: " + coins);
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.RED + "Nenhuma seção foi configurada.");
            }
        }
    }
}
