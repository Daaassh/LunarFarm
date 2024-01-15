package org.me.lunarfarm.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.managers.PlayerManager;

import java.io.IOException;
import java.util.List;

public class AreasInventory {
    private final PlayerManager manager;
    private final CustomFileConfiguration areas = new CustomFileConfiguration("areas.yml", Main.getPlugin(Main.class));

    public AreasInventory(PlayerManager manager) throws IOException, InvalidConfigurationException {
        this.manager = manager;
        setup();
    }
    private void setup(){
        createInventory();
    }

    public void createInventory() {
        Inventory inventory = Bukkit.createInventory(null, areas.getInt("areas.inventory.slots"), ChatColor.translateAlternateColorCodes('&', areas.getString("areas.inventory.name")));
        getItens(inventory);
    }
    private void getItens(Inventory inventory){
        for (String sec : areas.getConfigurationSection("areas").getKeys(false)) {
            ConfigurationSection section = areas.getConfigurationSection("areas");
            Material material = Material.getMaterial(section.getInt(sec + ".inventory.id"));
            if (!(material == null)){
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                if (!(meta == null)) {
                    meta.setDisplayName(translateColors(section.getString(sec + ".inventory.name")));
                    meta.setLore(translateColorsForList(section.getStringList(sec + ".inventory.lore")));
                    item.setItemMeta(meta);
                    setItens(inventory, section.getInt(sec + ".inventory.slot"), item);
                }
                else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "ItemMeta invalido");
                }
            }
            else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Material invalido");
            }
        }
    }

    private String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    private List<String> translateColorsForList(List<String> input){
        for (int i = 0; i < input.size(); i++) {
            input.set(i, translateColors(input.get(i)));
        }
        return input;
    }

    private void setItens(Inventory inv,Integer slot, ItemStack item){
        inv.setItem(slot, item);
        manager.getPlayer().openInventory(inv);
    }
}
