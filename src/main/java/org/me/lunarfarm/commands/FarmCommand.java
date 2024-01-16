package org.me.lunarfarm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.cache.PlayersInFarm;
import org.me.lunarfarm.inventory.InventoryPrincipal;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FarmCommand implements CommandExecutor {
    private List<Player> players = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cComando apenas para players");
            return true;
        }
        else {
            Player p = (Player) commandSender;
            if (PlayersInFarm.playersInFarm.containsKey(p.getUniqueId())) {
                p.sendMessage("§cVoce ja esta em uma fazenda, Digite /farm novamente para sair");
                players.add(p);
                return false;
            }
            if (players.contains(p)) {
                PlayersInFarm.playersInFarm.remove(p.getUniqueId());
                players.remove(p);
                p.sendMessage(ChatColor.GREEN + "Teletranportado para o spawn.");
                try {
                    p.teleport(getLocation());
                }
                catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Falha ao teleportar para o spawn.");
                    e.printStackTrace();
                }
            }

            if (p.hasPermission("lunarfarm.farm_open")) {
                try {
                    new InventoryPrincipal(p);
                }
                catch (IOException | InvalidConfigurationException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }
    private Location getLocation(){
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        double x = config.getDouble("spawn.location.x");
        double y = config.getDouble("spawn.location.y");
        double z = config.getDouble("spawn.location.z");
        double yaw = config.getDouble("spawn.location.yaw");
        double pitch = config.getDouble("spawn.location.pitch");
        World world = Bukkit.getWorld(config.getString("spawn.location.world"));
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }
}
