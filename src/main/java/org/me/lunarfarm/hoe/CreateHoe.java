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

import static java.lang.String.format;

public class CreateHoe {
    private PlayerManager manager;
    private CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));


    public CreateHoe(PlayerManager manager) throws IOException, InvalidConfigurationException {
        this.manager = manager;
        setup();
    }
    public void setup(){
        manager.getPlayer().getInventory().setItem(4, returnItem());
    }
    public ItemStack returnItem(){
        ItemStack item = new ItemStack(Material.WOOD_HOE);
        ItemMeta meta = item.getItemMeta();
        try {
            meta.spigot().setUnbreakable(true);
        }catch (Exception e) { e.printStackTrace(); }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',hoe.getString("name").replace("@blocks_break", getFormat((double) manager.getBlocks_break()))));
        meta.setLore(translateColors(hoe.getStringList("lore")));
        item.setItemMeta(meta);
        return item;
    }
    private List<String> translateColors(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, ChatColor.translateAlternateColorCodes('&', input.get(i)));
        }
        return input;
    }
    public String getFormat(Double valor) {
        String[] simbols = new String[]{"", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TD",
                "QD", "QID", "SD", "SSD", "OD", "ND", "V", "UV", "DV", "TV", "QV", "QQV", "SV", "SSV", "OV", "NV", "TG",
                "UTG", "DTG", "TTG", "QTG", "QQTG", "STG", "SSTG", "OTG", "NTG", "QG"};
        int index;
        for (index = 0; valor / 1000.0 >= 1.0; valor /= 1000.0, ++index) {
        }
        String valors = null;
        try {
            valors = format("%.0f", valor);
        }
        catch (Exception e) {
            valors = format("%.2f", valor);
        }
        return format(valors) + simbols[index];
    }
}
