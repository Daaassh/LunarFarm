package org.me.lunarfarm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.me.lunarfarm.commands.AdministratorCommands;
import org.me.lunarfarm.commands.FarmCommand;
import org.me.lunarfarm.configs.CustomFileConfiguration;
import org.me.lunarfarm.database.MySqlConnector;
import org.me.lunarfarm.events.*;
import org.me.lunarfarm.events.inventorys.Areas;
import org.me.lunarfarm.events.inventorys.Enchants;

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
            try {
                getCommand("farm").setExecutor(new FarmCommand());
                getCommand("farmadm").setExecutor(new AdministratorCommands());
                registerEvents();
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Comando /farm criado");
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Comando /farmadm criado");
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Eventos carregados.");

            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[ Lunar Farm] " + ChatColor.RED + "Erro ao registrar os eventos e comandos.");
                e.printStackTrace();
            }
            try {
                MySqlConnector.connect();
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "MySql conectado!");
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.RED + "Erro ao conectar com o mysql, Verifique sua config.yml!");
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Plugin carregado");
    }

    private void loadConfigs() throws IOException, InvalidConfigurationException {
        CustomFileConfiguration hoe = new CustomFileConfiguration("hoe.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration rewards = new CustomFileConfiguration("recompensas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration messages = new CustomFileConfiguration("messages.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration areas = new CustomFileConfiguration("areas.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration menus = new CustomFileConfiguration("menus.yml", Main.getPlugin(Main.class));
        CustomFileConfiguration farms = new CustomFileConfiguration("farms.yml", Main.getPlugin(Main.class));

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Configuracoes recarregadas");
    }
    private void registerEvents() throws IOException, InvalidConfigurationException {
        Bukkit.getPluginManager().registerEvents(new onQuit(), this);
        Bukkit.getPluginManager().registerEvents(new Enchants(), this);
        Bukkit.getPluginManager().registerEvents(new onDropItem(), this);
        Bukkit.getPluginManager().registerEvents(new onBreak(), this);
        Bukkit.getPluginManager().registerEvents(new onJoin(), this);
        Bukkit.getPluginManager().registerEvents(new onInventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new onInteract(), this);
        Bukkit.getPluginManager().registerEvents(new Areas(), this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + " [ Lunar Farm ] " + ChatColor.GREEN + "Eventos registrados");
    }
}
