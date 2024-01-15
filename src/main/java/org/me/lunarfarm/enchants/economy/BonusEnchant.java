package org.me.lunarfarm.enchants.economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.managers.PlayerManager;

public class BonusEnchant {
    private final PlayerManager manager;
    private  final FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    public BonusEnchant(PlayerManager manager) {
        this.manager = manager;
    }
    public Double setup() {
        Double chances = manager.getBonus() * config.getDouble("bonus.increase-for-level");
        return chances;
    }
}
