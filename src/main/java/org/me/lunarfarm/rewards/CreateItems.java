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
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.enchants.economy.BonusEnchant;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.porcentage.PorcentageManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CreateItems {
    private final Player player;
    private final TitleAPI api = new TitleAPI();

    public CreateItems(Player player) throws IOException, InvalidConfigurationException, SQLException {
        this.player = player;
        setup();
    }

    private void setup() throws IOException, InvalidConfigurationException {
        Main plugin = Main.getPlugin(Main.class);
        CustomFileConfiguration config = new CustomFileConfiguration("recompensas.yml", plugin);
        String sectionName = "rewards.itens";
        PlayerManager managers = PlayersInFarm.cache.getIfPresent(player.getUniqueId());

        ConfigurationSection section = config.getConfigurationSection(sectionName);
        if (section != null) {
            for (String itemName : section.getKeys(false)) {
                double chance = section.getDouble(itemName + ".chance") +  new BonusEnchant(managers).setup();
                PorcentageManager manager = new PorcentageManager(chance);

                if (manager.setup()) {
                    createItemFromConfig(section, itemName);
                    api.sendFullTitle(player, 1,1,2,ChatColor.YELLOW + "Recompensas", "Você recebeu uma recompensa");
                }
            }
        }
    }

    private void createItemFromConfig(ConfigurationSection section, String itemName) {
        Material material = Material.getMaterial(section.getInt(itemName + ".id"));
        if (!(material == null)) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(translateColors(section.getString(itemName + ".name")));
                List<String> lore = section.getStringList(itemName + ".lore");
                meta.setLore(translateColors(lore));
                item.setItemMeta(meta);
                if (section.getBoolean(itemName + "message.usage")) {
                    player.sendMessage(translateColors(section.getString(itemName + "message.msg").replace("@item", section.getString(itemName + ".name"))));
                }
                if (section.getBoolean(itemName + ".commands.usage")) {
                    try {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), section.getString(itemName + ".commands.command").replace("{player}", player.getDisplayName()));
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Comando inválido para " + itemName);
                        e.printStackTrace();
                    }
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

