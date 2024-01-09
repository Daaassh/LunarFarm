package org.me.lunarfarm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;

import java.io.IOException;

public class AdministratorCommands implements CommandExecutor {
    private CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));
    private CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
    private CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
    private CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));

    public AdministratorCommands() throws IOException, InvalidConfigurationException {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("lunarfarm.farmadm")) {
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    hoe.reload();
                    rewards.reload();
                    messages.reload();
                    menus.reload();
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("msg-on-reload-config")));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("args-error")));
                return false;
            }
            return true;
        }
        else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("no-permission")));
            return false;
        }
    }
}
