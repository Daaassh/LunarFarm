package org.me.lunarfarm.enchants.economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.managers.PlayerManager;

public class MultiplicadorEnchant {
    private final Integer blocks;
    private final PlayerManager manager;
    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    public MultiplicadorEnchant(PlayerManager manager,Integer blocks) {
        this.blocks = blocks;
        this.manager = manager;
        setup();
    }
    public void setup(){
        double percentage = config.getDouble("multiplicador.increase-for-level");
        manager.setSeeds((long) (manager.getSeeds() + (blocks * percentage)));
    }
}
