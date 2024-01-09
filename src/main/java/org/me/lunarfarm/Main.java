package org.me.lunarfarm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.me.lunarfarm.commands.AdministratorCommands;
import org.me.lunarfarm.commands.FarmCommand;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlConnector;
import org.me.lunarfarm.events.onBreak;
import org.me.lunarfarm.events.onInventoryClick;
import org.me.lunarfarm.events.onJoin;
import java.io.IOException;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            loadConfigs();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        try {
            MySqlConnector.connect();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "MySql conectado!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            registerEvents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Plugin carregado");
        try {
            getCommand("farm").setExecutor(new FarmCommand());
            getCommand("farmadm").setExecutor(new AdministratorCommands());
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Comando /farm criado");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.RED + "Erro ao criar o comando /farm");
            throw new RuntimeException(e);
        }
    }


    private void loadConfigs() throws IOException, InvalidConfigurationException {
        CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Configs recarregadas");
    }
    private void registerEvents() throws IOException, InvalidConfigurationException {
        getServer().getPluginManager().registerEvents(new onBreak(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        getServer().getPluginManager().registerEvents(new onInventoryClick(), this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Events registrados");
    }
}
