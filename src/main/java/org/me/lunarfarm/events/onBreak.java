package org.me.lunarfarm.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.api.PrimeActionbar;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlUtils;
import org.me.lunarfarm.enchants.economy.BonusEnchant;
import org.me.lunarfarm.enchants.economy.FortunaEnchant;
import org.me.lunarfarm.enchants.economy.MultiplicadorEnchant;
import org.me.lunarfarm.hoe.CreateHoe;
import org.me.lunarfarm.managers.PlayerManager;
import org.me.lunarfarm.porcentage.PorcentageManager;
import org.me.lunarfarm.rewards.CreateItems;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.String.format;

public class onBreak implements Listener {
    private final CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));
    private final List<Material> farm = new ArrayList<>();
    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    public onBreak() throws IOException, InvalidConfigurationException {
    }

    @EventHandler
    public void BreakBlocks(BlockBreakEvent e) throws SQLException, IOException, InvalidConfigurationException {
        int i = 0;
        materialList();
        if (PlayersInFarm.cache.getIfPresent(e.getPlayer().getUniqueId()) != null) {
            Player p = e.getPlayer();
            PlayerManager manager = PlayersInFarm.cache.getIfPresent(p.getUniqueId());
            if (farm.contains(e.getBlock().getType()) || e.getBlock().getType() == Material.CROPS) {
                if (p.hasPermission("lunarfarm.break_farms")) {
                    if (p.getItemInHand().getType() == Material.WOOD_HOE) {
                        assert manager != null;
                        if (i == config.getInt("break_farms.blocks-to-update")) {
                            MySqlUtils.updatePlayer(manager, p);
                            PlayersInFarm.cache.invalidate(p.getUniqueId());
                            PlayersInFarm.cache.put(p.getUniqueId(), manager);
                            p.getInventory().setItem(5, new CreateHoe(manager).returnItem());
                            p.sendMessage("Seus dados foram atualizados.");
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Valor ate agora.");
                        }

                        manager.setBlocks_break(manager.getBlocks_break() + 1);
                        new MultiplicadorEnchant(manager, 1);
                        new FortunaEnchant(manager, e.getBlock().getType());
                        if (config.getBoolean("break_farms.title-api")) {
                            PrimeActionbar.sendActionbar(p, ChatColor.translateAlternateColorCodes('&', messages.getString("message-on-break-block").replace("@sementes", getFormat(manager.getSeeds()))));
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("message-on-break-block").replace("@sementes", String.valueOf(manager.getSeeds()))));
                        }
                        double chances = new BonusEnchant(manager).setup();
                        chances += rewards.getDouble("rewards.chance-initial");
                        if (new PorcentageManager(chances).setup()) {
                            new CreateItems(p);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                e.getBlock().setType(Material.CROPS);
                            }
                        }.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 20L);
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
                    }
                }
            }
        }
        else {
            PlayersInFarm.cache.put(e.getPlayer().getUniqueId(), MySqlUtils.getPlayer(e.getPlayer()));
        }
    }

        private void materialList () {
            for (String list : farms.getConfigurationSection("farms").getKeys(false)) {
                farm.add(Material.getMaterial(farms.getString("farms." + list).toUpperCase()));
            }
        }
    public String getFormat(double valor) {
        String[] simbols = new String[]{"", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TD",
                "QD", "QID", "SD", "SSD", "OD", "ND", "V", "UV", "DV", "TV", "QV", "QQV", "SV", "SSV", "OV", "NV", "TG",
                "UTG", "DTG", "TTG", "QTG", "QQTG", "STG", "SSTG", "OTG", "NTG", "QG"};
        int index;
        for (index = 0; valor / 1000.0 >= 1.0; valor /= 1000.0, ++index) {
        }
        String valors = format("%.2f", valor);
        return format(valors) + simbols[index];
    }
   
}
