package org.me.lunarfarm.enchants.economy;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.managers.PlayerManager;
import java.io.IOException;


public class FortunaEnchant {
    private final PlayerManager manager;
    private final Material item;
    private final FileConfiguration configuration = Main.getPlugin(Main.class).getConfig();
    private final CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));


    public FortunaEnchant(PlayerManager manager, Material item) throws IOException, InvalidConfigurationException {
        this.manager = manager;
        this.item = item;
        setup();
    }
    private void setup(){
        double coins = manager.getFortuna() * configuration.getDouble("fortune.increase-for-level");
        for (String sec : farms.getConfigurationSection("farms").getKeys(false)) {
            Material material = Material.getMaterial(sec);
            if (material == item) {
                double coin = coins + farms.getDouble("farms." + sec.toUpperCase() + ".coins");
                manager.setSeeds(manager.getSeeds() + farms.getInt("farms." + sec + ".seeds"));
                manager.getPlayer().sendMessage("Coins ao quebrar: " + coin);
            }
        }
    }

}
