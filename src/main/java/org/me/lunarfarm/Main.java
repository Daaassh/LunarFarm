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
import org.me.lunarfarm.events.onInteract;
import org.me.lunarfarm.events.onInventoryClick;
import org.me.lunarfarm.events.onJoin;
import java.io.IOException;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        try {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "\n" +
                    "    __                               ____  __            _           \n" +
                    "   / /   __  ______  ____ ______    / __ \\/ /_  ______ _(_)___  _____\n" +
                    "  / /   / / / / __ \\/ __ `/ ___/   / /_/ / / / / / __ `/ / __ \\/ ___/\n" +
                    " / /___/ /_/ / / / / /_/ / /      / ____/ / /_/ / /_/ / / / / (__  ) \n" +
                    "/_____/\\__,_/_/ /_/\\__,_/_/      /_/   /_/\\__,_/\\__, /_/_/ /_/____/  \n" +
                    "                                               /____/                \n");

            saveDefaultConfig();
            loadConfigs();
            getCommand("farm").setExecutor(new FarmCommand());
            getCommand("farmadm").setExecutor(new AdministratorCommands());
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Comando /farm criado");
            try {
                MySqlConnector.connect();
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "MySql conectado!");
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.RED + "Erro ao conectar com o mysql, Verifique sua config.yml!");
            }
            registerEvents();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Plugin carregado");
    }

    private void loadConfigs() throws IOException, InvalidConfigurationException {
        CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Configs recarregadas");
    }
    private void registerEvents() throws IOException, InvalidConfigurationException {
        getServer().getPluginManager().registerEvents(new onBreak(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        getServer().getPluginManager().registerEvents(new onInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new onInteract(), this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Events registrados");
    }
}
