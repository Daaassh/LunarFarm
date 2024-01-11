package org.me.lunarfarm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.me.lunarfarm.inventory.InventoryPrincipal;
import java.io.IOException;
import java.sql.SQLException;


public class FarmCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cComando apenas para players");
            return true;
        }
        else {
            Player p = (Player) commandSender;
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
}
