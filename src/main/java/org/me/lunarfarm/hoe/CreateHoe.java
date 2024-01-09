package org.me.lunarfarm.hoe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;
import java.util.List;

public class CreateHoe {
    private PlayerManager manager;
    private CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));


    public CreateHoe(PlayerManager manager) throws IOException, InvalidConfigurationException {
        this.manager = manager;
        setup();
    }
    public void setup(){
        ItemStack item = new ItemStack(Material.WOOD_HOE);
        ItemMeta meta = item.getItemMeta();
        try {
            meta.spigot().setUnbreakable(true);
        }catch (Exception e) { e.printStackTrace(); }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',hoe.getString("name").replace("@blocks_break", String.valueOf(manager.getBlocks_break()))));
        meta.setLore(translateColors(hoe.getStringList("lore")));

        item.setItemMeta(meta);
        manager.getPlayer().getInventory().setItem(1, item);
    }
    private List<String> translateColors(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, ChatColor.translateAlternateColorCodes('&', input.get(i)));
        }
        return input;
    }
}
