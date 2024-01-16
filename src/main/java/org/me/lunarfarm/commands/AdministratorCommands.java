package org.me.lunarfarm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;

import java.io.IOException;

public class AdministratorCommands implements CommandExecutor {
    private final CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));
    private final CustomFileConfiguration areas = new CustomFileConfiguration("areas.yml", Main.getPlugin(Main.class));
    public AdministratorCommands() throws IOException, InvalidConfigurationException {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("lunarfarm.farmadm")) {
            if (args.length == 0) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("args-error")));
                return false;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    Main.getPlugin(Main.class).reloadConfig();
                    hoe.reload();
                    rewards.reload();
                    messages.reload();
                    menus.reload();
                    farms.reload();
                    areas.reload();
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("msg-on-reload-config")));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (args[0].equalsIgnoreCase("set")) {
                String identifier = args[1];
                setLocations(identifier, (Player) commandSender);
            }
            if (args[0].equalsIgnoreCase("setspawn")) {
                setSpawn((Player) commandSender);
            }
            return true;
        }
        else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
            return false;
        }
    }
    private void setLocations(String identifier, Player player) {
        if (areas.getDouble("areas." + identifier + ".location.x") == player.getLocation().getX()) {
            player.sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Ja existe uma localização x salva ai!");
        }
        else {
            areas.set("areas." + identifier + ".location.x", player.getLocation().getX());
            areas.set("areas." + identifier + ".location.y", player.getLocation().getY());
            areas.set("areas." + identifier + ".location.z", player.getLocation().getZ());
            areas.set("areas." + identifier + ".location.yaw", player.getLocation().getYaw());
            areas.set("areas." + identifier + ".location.pitch", player.getLocation().getPitch());
            areas.set("areas." + identifier + ".location.world", player.getLocation().getWorld().getName());
            areas.save();
            player.sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + " Localização salva com sucesso, Identificador: " + identifier);
        }
    }
    private void setSpawn(Player player) {
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        config.set("spawn.location.x", player.getLocation().getX());
        config.set("sṕawn.location.y", player.getLocation().getY());
        config.set("spawn.location.z", player.getLocation().getZ());
        config.set("spawn.location.yaw", player.getLocation().getYaw());
        config.set("spawn.location.pitch", player.getLocation().getPitch());
        config.set("spawn.location.world", player.getLocation().getWorld().getName());
        Main.getPlugin(Main.class).saveConfig();
        Main.getPlugin(Main.class).reloadConfig();
        player.sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + " Localização salva com sucesso");
    }
}
