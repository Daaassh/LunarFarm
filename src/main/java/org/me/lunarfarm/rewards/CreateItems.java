package org.me.lunarfarm.rewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.api.TitleAPI;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.porcentage.PorcentageManager;

import java.io.IOException;
import java.util.List;

public class CreateItems {
    private Player player;
    private TitleAPI api = new TitleAPI();

    public CreateItems(Player player) throws IOException, InvalidConfigurationException {
        this.player = player;
        setup();
    }

    private void setup() throws IOException, InvalidConfigurationException {
        Main plugin = Main.getPlugin(Main.class);
        CustomFileConfiguration config = new CustomFileConfiguration("recompensas.yml", plugin);

        String sectionName = "rewards.itens";

        ConfigurationSection section = config.getConfigurationSection(sectionName);
        if (section != null) {
            for (String itemName : section.getKeys(false)) {
                double chance = section.getDouble(itemName + ".chance");
                PorcentageManager manager = new PorcentageManager(chance);

                if (manager.setup()) {
                    createItemFromConfig(section, itemName);
                    api.sendFullTitle(player, 1,1,2,ChatColor.YELLOW + "Recompensas", "Você recebeu uma recompensa");
                }
            }
        }
    }

    private void createItemFromConfig(ConfigurationSection section, String itemName) {
        Material material = Material.getMaterial(itemName + ".id");
        if (material != null) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(translateColors(section.getString(itemName + ".name")));
                List<String> lore = section.getStringList(itemName + ".lore");
                meta.setLore(translateColors(lore));
                item.setItemMeta(meta);
                if (section.getBoolean(itemName + ".commands.usage")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), section.getString(itemName + ".command").replace("{player}", player.getDisplayName()));
                } else {
                    player.getInventory().addItem(item);
                }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Material inválido para " + itemName);
        }
    }

    private String translateColors(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private List<String> translateColors(List<String> input) {
        input.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        return input;
    }
}

